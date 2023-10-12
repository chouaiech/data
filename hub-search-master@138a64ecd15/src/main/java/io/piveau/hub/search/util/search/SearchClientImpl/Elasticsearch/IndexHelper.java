package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.elasticsearch.client.indexlifecycle.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.cluster.metadata.Template;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class IndexHelper {
    private static final Logger LOG = LoggerFactory.getLogger(IndexHelper.class);
    public static void processPayloadLanguageFields(JsonObject payload) {
        JsonArray language = payload.getJsonArray("language");

        boolean titleSet = false;
        boolean descriptionSet = false;

        if (language != null && !language.isEmpty()) {
            for (Object obj : language) {
                JsonObject payloadLanguageJson = (JsonObject) obj;
                String payloadLanguage = payloadLanguageJson.getString("id");

                if (payloadLanguage != null && !payloadLanguage.isEmpty()) {
                    JsonObject title = payload.getJsonObject("title");
                    if (title != null && !title.isEmpty()) {
                        if (!titleSet && title.getString(payloadLanguage) != null) {
                            title.put("_lang", title.getString(payloadLanguage));
                            titleSet = true;
                        }
                    }

                    JsonObject description = payload.getJsonObject("description");
                    if (description != null && !description.isEmpty()) {
                        if (!descriptionSet && description.getString(payloadLanguage) != null) {
                            description.put("_lang", description.getString(payloadLanguage));
                            descriptionSet = true;
                        }
                    }
                }

                if (titleSet && descriptionSet) {
                    return;
                }
            }
        }

        if (!titleSet) {
            JsonObject title = payload.getJsonObject("title");
            if (title != null && !title.isEmpty()) {
                if (title.getString("en") != null) {
                    title.put("_lang", title.getString("en"));
                } else {
                    Iterator<String> it = title.getMap().keySet().iterator();
                    title.put("_lang", title.getString(it.next()));
                }
            }
        }

        if (!descriptionSet) {
            JsonObject description = payload.getJsonObject("description");
            if (description != null && !description.isEmpty()) {
                if (description.getString("en") != null) {
                    description.put("_lang", description.getString("en"));
                } else {
                    Iterator<String> it = description.getMap().keySet().iterator();
                    description.put("_lang", description.getString(it.next()));
                }
            }
        }
    }

    public static void processResultLanguageFields(JsonObject result) {
        JsonObject title = result.getJsonObject("title");
        if (title != null && !title.isEmpty()) {
            title.remove("_lang");
        }

        JsonObject description = result.getJsonObject("description");
        if (description != null && !description.isEmpty()) {
            description.remove("_lang");
        }
    }

    public static Promise<Map<String, Phase>> generatePhaseMap(String policyJson) {
        Promise<Map<String, Phase>> promise = Promise.promise();

        JsonObject policy = new JsonObject(policyJson);

        JsonObject phasesJson = policy.getJsonObject("policy").getJsonObject("phases");

        Map<String, Phase> phases = new HashMap<>();
        phasesJson.stream().iterator().forEachRemaining(stringObjectEntry -> {
            Map<String, LifecycleAction> actions = new HashMap<>();
            String phasename = stringObjectEntry.getKey();
            JsonObject phase = (JsonObject) stringObjectEntry.getValue();


            phase.getJsonObject("actions").stream().iterator().forEachRemaining(actionEntry -> {
                JsonObject actionObject = (JsonObject) actionEntry.getValue();

                LifecycleAction lifecycleAction;
                ByteSizeValue bsv = ByteSizeValue.parseBytesSizeValue(actionObject.getString("max_size"),
                        phasename + "- rollover - max_size");
                switch (actionEntry.getKey()) {
                    case AllocateAction.NAME:

                        try {
                            Map<String, String> include =
                                    new ObjectMapper().readValue(actionObject.getJsonObject("include").encodePrettily(), HashMap.class);

                            Map<String, String> exclude =
                                    new ObjectMapper().readValue(actionObject.getJsonObject("exclude").encodePrettily(), HashMap.class);

                            Map<String, String> require =
                                    new ObjectMapper().readValue(actionObject.getJsonObject("require").encodePrettily(), HashMap.class);

                            lifecycleAction = new AllocateAction(actionObject.getInteger("number_of_replicas"), include, exclude, require);
                        } catch (JsonProcessingException e) {
                            promise.fail(e);
                            return;
                        }
                        break;
                    case DeleteAction.NAME:
                        lifecycleAction = new DeleteAction();
                        break;
                    case ForceMergeAction.NAME:
                        lifecycleAction = new ForceMergeAction(actionObject.getInteger("max_num_segments"));
                        break;
                    case FreezeAction.NAME:
                        lifecycleAction = new FreezeAction();
                        break;
                    case MigrateAction.NAME:
                        lifecycleAction = new MigrateAction(actionObject.getBoolean("enabled", false));
                        break;
                    case ReadOnlyAction.NAME:
                        lifecycleAction = new ReadOnlyAction();
                        break;
                    case RolloverAction.NAME:
                        TimeValue timeValue = TimeValue.parseTimeValue(actionObject.getString("max_age"), phasename + "- rollover - max_age");
                        Long maxDocs = actionObject.getLong("max_docs");
                        lifecycleAction = new RolloverAction(bsv, bsv, timeValue, maxDocs);
                        break;
                    case SearchableSnapshotAction.NAME:
                        lifecycleAction = new SearchableSnapshotAction(
                                actionObject.getString("snapshot_repository"),
                                actionObject.getBoolean("force_merge_index"));
                        break;
                    case SetPriorityAction.NAME:
                        lifecycleAction = new SetPriorityAction(actionObject.getInteger("priority"));
                        break;
                    case ShrinkAction.NAME:
                        lifecycleAction = new ShrinkAction(actionObject.getInteger("number_of_shards"), bsv);
                        break;
                    case UnfollowAction.NAME:
                        lifecycleAction = new UnfollowAction();
                        break;
                    case WaitForSnapshotAction.NAME:
                        lifecycleAction = new WaitForSnapshotAction(actionObject.getString("policy"));
                        break;
                    default:
                        promise.fail("Unknown action: " + actionEntry.getKey());
                        return;
                }
                actions.putIfAbsent(actionEntry.getKey(), lifecycleAction);
            });

            TimeValue timeValue = TimeValue.parseTimeValue(phase.getString("min_age"), TimeValue.ZERO, phasename + "min_age");
            phases.putIfAbsent(phasename, new Phase(phasename, timeValue, actions));
        });
        promise.complete(phases);
        return promise;
    }


    public static ComposableIndexTemplate generateIndexTemplate(String templatestring, String settingsstring,
                                                                String mappingstring) {

        JsonObject indexTemplate = new JsonObject(templatestring);
        JsonObject baseSettings = new JsonObject(settingsstring);
        JsonObject mapping = new JsonObject(mappingstring);

        List<String> index_patterns = new ArrayList<>();

        JsonArray itp = indexTemplate.getJsonArray("index_patterns", new JsonArray());
        for (int i = 0; i < itp.size(); i++) {
            index_patterns.add(itp.getString(i));
        }
        JsonObject templatejson = indexTemplate.getJsonObject("template", new JsonObject());
        JsonObject settingsJson = templatejson.getJsonObject("settings", new JsonObject());
        JsonObject mergedSettingsJson = settingsJson.mergeIn(baseSettings);

        Settings settings = Settings.builder().loadFromMap(mergedSettingsJson.getMap()).build();

        CompressedXContent mappings = null;
        try {
            mappings = new CompressedXContent(mapping.toString());
        } catch (IOException e) {
            LOG.error("IndexHelper: " + e);
            // e.printStackTrace();
        }

        JsonObject aliasesjson = templatejson.getJsonObject("aliases", new JsonObject());
        Map<String, AliasMetadata> aliases = new HashMap<>();

        aliasesjson.fieldNames().forEach(alias->{
            JsonObject aliasjson = aliasesjson.getJsonObject(alias);

            AliasMetadata am = AliasMetadata.newAliasMetadataBuilder(alias)
                    .filter(aliasjson.getString("filter"))
                    .indexRouting(aliasjson.getString("index_routing"))
                    .routing(aliasjson.getString("routing"))
                    .searchRouting(aliasjson.getString("search_routing"))
                    .isHidden(aliasjson.getBoolean("is_hidden"))
                    .writeIndex(aliasjson.getBoolean("is_write_index")).build();

            aliases.putIfAbsent(alias,am);

        });

        Template template = new Template(settings, mappings, aliases);

        Long version = indexTemplate.getLong("version");
        Long priority = indexTemplate.getLong("priority");
        Map<String,Object> meta = indexTemplate.getJsonObject("_meta",new JsonObject()).getMap();

        List<String> componentTemplates = new ArrayList<>();

        JsonArray componentlist = indexTemplate.getJsonArray("component_templates", new JsonArray());

        for (int i = 0; i < componentlist.size(); i++) {
            componentTemplates.add(componentlist.getString(i));
        }

        return new ComposableIndexTemplate(index_patterns,template,componentTemplates,priority,version,meta);
    }
}
