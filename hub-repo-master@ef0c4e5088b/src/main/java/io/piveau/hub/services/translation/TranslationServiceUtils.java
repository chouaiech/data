package io.piveau.hub.services.translation;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.vocabularies.vocabulary.EDP;
import io.vertx.core.json.JsonObject;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TranslationServiceUtils {

    public static JsonObject callbackParameters;
    public static List<String> translationLanguages;

    public static String buildLanguageTag(String originalLanguage, String targetLanguage) {
        if (targetLanguage.equals("nb")) {
            targetLanguage = "no";
        }
        return targetLanguage + "-t-" + originalLanguage + "-t0-mtec";
    }

    public static boolean isTranslationCompleted(DatasetHelper helper) {
        return helper != null && helper.recordResource().hasProperty(EDP.transStatus, EDP.TransCompleted);
    }

    public static Map<String, String> propertyToLanguageMap(Resource resource, Property property, String defaultLanguage) {
        LinkedHashMap<String, String> languages = new LinkedHashMap<>();
        resource.listProperties(property)
                .filterKeep(stm -> stm.getObject().isLiteral())
                .filterDrop(stm -> stm.getLiteral().getString().isBlank())
                .filterDrop(stm -> stm.getLiteral().getLanguage().endsWith("-t0-mtec"))
                .forEachRemaining(statement -> {
                    Literal obj = statement.getObject().asLiteral();
                    if (obj.getLanguage().isBlank()) {
                        languages.put(defaultLanguage, obj.getString());
                    } else {
                        languages.put(obj.getLanguage(), obj.getString());
                    }
                });
        return languages;
    }

    public static JsonObject buildTranslationRequest(DatasetHelper helper, DatasetHelper oldHelper, Boolean force) {
        String defaultLanguage = helper.sourceLang();

        Resource oldResource = isTranslationCompleted(oldHelper) ? oldHelper.resource() : null;

        JsonObject dict = new JsonObject();

        JsonObject datasetSubject = new JsonObject();

        // Dataset title
        JsonObject datasetTitleProperty = buildDictEntry(helper.resource(), oldResource, DCTerms.title, defaultLanguage);
        if (!datasetTitleProperty.isEmpty()) {
            datasetSubject.put(DCTerms.title.getURI(), datasetTitleProperty);
        }

        if (!datasetSubject.isEmpty()) {
            dict.put(helper.resource().getURI(), datasetSubject);
        }

        // Dataset description
        JsonObject datasetDescriptionProperty = buildDictEntry(helper.resource(), oldResource, DCTerms.description, defaultLanguage);
        if (!datasetDescriptionProperty.isEmpty()) {
            datasetSubject.put(DCTerms.description.getURI(), datasetDescriptionProperty);
        }

        // iterate through distributions
        helper.resource().listProperties(DCAT.distribution).forEachRemaining(statement -> {
            if (statement.getObject().isURIResource()) {
                Resource newDistribution = statement.getObject().asResource();
                Resource oldDistribution = oldResource != null ?
                        oldResource.getModel().getResource(newDistribution.getURI()) : null;

                JsonObject distributionSubject = new JsonObject();

                JsonObject distributionTitleProperty = buildDictEntry(newDistribution, oldDistribution, DCTerms.title, defaultLanguage);
                if (!distributionTitleProperty.isEmpty()) {
                    distributionSubject.put(DCTerms.title.getURI(), distributionTitleProperty);
                }

                JsonObject distributionDescriptionProperty = buildDictEntry(newDistribution, oldDistribution, DCTerms.description, defaultLanguage);
                if (!distributionDescriptionProperty.isEmpty()) {
                    distributionSubject.put(DCTerms.description.getURI(), distributionDescriptionProperty);
                }

                if (!distributionSubject.isEmpty()) {
                    dict.put(newDistribution.getURI(), distributionSubject);
                }
            }
        });

        JsonObject translationRequest = new JsonObject();
        if (!dict.isEmpty()) {
            translationRequest.put("id", helper.uriRef());
            translationRequest.put("data", new JsonObject()
                    .put("catalogueId", helper.catalogueId())
                    .put("defaultLanguage", helper.sourceLang()));
            translationRequest.put("dict", dict);
            translationRequest.put("callback", callbackParameters);
        }

        return translationRequest;
    }

    private static JsonObject buildDictEntry(Resource resource, Resource oldResource, Property property, String defaultLanguage) {

        Map<String, String> propertyLanguages = propertyToLanguageMap(resource, property, defaultLanguage);
        if (propertyLanguages.isEmpty()) {
            return new JsonObject();
        }

        String language = defaultLanguage;
        if (!propertyLanguages.containsKey(defaultLanguage)) {
            language = propertyLanguages.keySet().iterator().next();
        }

        String value = propertyLanguages.get(language);

        if (oldResource != null) {
            Map<String, String> oldMap = propertyToLanguageMap(oldResource, property, defaultLanguage);
            if (value.equals(oldMap.get(language))) {
                getAllTranslations(oldResource, property)
                        .forEach(literal -> resource.addLiteral(property, literal));
                return new JsonObject();
            }
        }

        List<String> targetLanguages = new ArrayList<>(translationLanguages);
        targetLanguages.removeAll(propertyLanguages.keySet());

        JsonObject entry = new JsonObject();
        if (!targetLanguages.isEmpty()) {
            entry
                    .put("value", value)
                    .put("sourceLanguage", language)
                    .put("targetLanguages", targetLanguages);
        }

        return entry;
    }

    private static List<Literal> getAllTranslations(Resource resource, Property property) {
        return resource.listProperties(property)
                .filterKeep(stm -> stm.getObject().isLiteral())
                .filterKeep(stm -> stm.getLanguage().endsWith("-t0-mtec"))
                .mapWith(Statement::getLiteral)
                .toList();
    }

    private static void removeAllTranslations(Resource resource, Property property) {
        List<Statement> statements = resource.listProperties(property)
                .filterKeep(stm -> stm.getObject().isLiteral())
                .filterKeep(stm -> stm.getLanguage().endsWith("-t0-mtec"))
                .toList();
        resource.getModel().remove(statements);
    }

    public static void applyTranslations(Model model, JsonObject dict) {
        dict.forEach(entry -> {
            Resource resource = model.getResource(entry.getKey());
            JsonObject properties = (JsonObject) entry.getValue();
            properties.forEach(propEntries -> {
                Property property = model.getProperty(propEntries.getKey());
                removeAllTranslations(resource, property);
                JsonObject propertyValues = (JsonObject) propEntries.getValue();
                String sourceLanguage = propertyValues.getString("sourceLanguage");
                JsonObject translations = propertyValues.getJsonObject("translations");
                translations.getMap().forEach((lang, value) -> {
                    if (value instanceof String) {
                        Literal literal = model.createLiteral((String) value, buildLanguageTag(sourceLanguage, lang));
                        resource.addProperty(property, literal);
                    }
                });
            });
        });
    }

}
