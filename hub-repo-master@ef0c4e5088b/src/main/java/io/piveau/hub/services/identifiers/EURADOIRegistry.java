package io.piveau.hub.services.identifiers;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.indexing.Indexing;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom2.Document;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EURADOIRegistry extends IdentifierRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String user;
    private final String password;
    private final String apiURL;
    private final String prefix;
    private final String doiBaseURL = "https://doi.org/";
    private final Random random;

    public EURADOIRegistry(Vertx vertx, WebClient webClient, JsonObject config) {
        super(vertx, webClient, config);
        user = config.getString("user");
        password = config.getString("password");
        apiURL = config.getString("api");
        prefix = config.getString("prefix");
        random = new SecureRandom();
    }

    @Override
    public String getName() {
        return "EU RA DOI Registry";
    }

    @Override
    public Future<Identifier> getIdentifier(String URI, DatasetHelper dataset, List<Identifier> existingIdentifiers) {
        Promise<Identifier> promise = Promise.promise();
        Identifier identifier =  new Identifier();

        getNextDOI(existingIdentifiers).onSuccess(ar -> {
            JsonObject metadata = Indexing.indexingDataset(dataset.resource(), dataset.recordResource(), null, "en");
            try {
                String xml = buildRequestXML(ar, URI, metadata);
                registerDOI(xml).onSuccess(arRegister -> {
                    identifier.issued = ZonedDateTime.now(ZoneOffset.UTC);
                    identifier.identifierURI = doiBaseURL + arRegister.getString("first_doi");
                    identifier.schema = "http://publications.europa.eu/resource/authority/notation-type/DOI";
                    identifier.creatorURI = "http://publications.europa.eu/resource/authority/corporate-body/PUBL";
                    identifier.identifier = arRegister.getString("first_doi");
                   promise.complete(identifier);
                }).onFailure(failure -> {
                   promise.fail(failure.getMessage());
                });
            } catch (Exception e) {
                promise.fail(e.getMessage());
            }
        }).onFailure(failure -> {
            promise.fail(failure.getMessage());
        });
        return promise.future();
    }

    /**
     * This methods performs the actual request to the EU-RA
     *
     * @param requestXML
     * @return
     */
    public Future<JsonObject> registerDOI(String requestXML) {
        Promise<JsonObject> promise = Promise.promise();
        webClient
                .postAbs(apiURL)
                .expect(ResponsePredicate.SC_OK)
                .putHeader("Content-Type", "application/xml")
                .basicAuthentication(user, password)
                .sendBuffer(Buffer.buffer(requestXML), ar -> {
                    if(ar.succeeded()) {
                        promise.complete(ar.result().bodyAsJsonObject());
                    } else {
                        promise.fail(ar.cause().getMessage());
                    }
                });
        return promise.future();
    }

    /**
     * Generate a valid DOI URL
     *
     * @return
     */
    private Future<String> getNextDOI(List<Identifier> existingIdentifiers) {
        Promise<String> promise = Promise.promise();

        // Do we have already a DOI?
        checkIdentifiers(existingIdentifiers).onFailure(result -> {
            // If net find a new one
            checkDOIAvailability(getRandomDOIPath(), ar -> {
                if(ar.succeeded()) {
                    promise.complete(ar.result());
                } else {
                    promise.fail("Could not find a free DOI URL");
                }
            });
        }).onSuccess(promise::complete);
        return promise.future();
    }

    /**
     * Check the exisiting Identifiers, if the the current type is already registered
     * @param existingIdentifiers
     * @return
     */
    private Future<String> checkIdentifiers(List<Identifier> existingIdentifiers) {
        Promise<String> promise = Promise.promise();

        String result = null;
        // ToDo Find a more elegant way to do that
        if(existingIdentifiers.size() > 0) {
            for (Identifier identifier : existingIdentifiers) {
                if (identifier.identifierURI.startsWith(doiBaseURL + prefix)) {
                    logger.info("DOI already set");
                    result = identifier.identifier;
                    break;
                }
            }
        }

        if(result != null) {
            promise.complete(result);
        } else {
            promise.fail("Identifier does not exit");
        }
        return promise.future();
    }

    /**
     * Check if the random DOI URL is available by performing head requests
     * If it is already taken another random URL is generated and so on.
     *
     * ToDo Add a cancelation point
     * @param url
     * @param result
     */
    private void checkDOIAvailability(String url, Handler<AsyncResult<String>> result) {
        webClient
                .headAbs(doiBaseURL + url)
                .expect(ResponsePredicate.SC_NOT_FOUND)
                .send(ar -> {
                    if(ar.succeeded()) {
                        result.handle(Future.succeededFuture(url));
                    } else {
                        checkDOIAvailability(getRandomDOIPath(), result);
                    }
                });
    }

    /**
     * Generates the random DOI URL
     * @return
     */
    private String getRandomDOIPath() {
        return prefix + "/" + getRandomID();
    }

    /**
     * Generates a random 15 character long numeric string
     * @return
     */
    private String getRandomID() {
        int left = 48; // 0
        int right = 57; // 9
        int length = 15;

        return random.ints(left, right + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Builds the XML for the request to the registry endpoint
     *
     * @return
     */
    private String buildRequestXML(String doi, String datasetURL, JsonObject dataset) throws IdentifierException {
        Document document = new Document();
        Namespace ns =  Namespace.getNamespace("http://ra.publications.europa.eu/schema/doidata/1.0");
        Element root = new Element("DOIRegistrationMessage", ns);
        root.addNamespaceDeclaration(Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        root.
                setAttribute("schemaLocation",
                        "http://ra.publications.europa.eu/schema/doidata/1.0  https://ra.publications.europa.eu/schema/OP/DOIMetadata/1.0/OP_DOIMetadata_1.0.xsd",
                        Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));


        Element header = new Element("Header", ns);
        header.addContent(new Element("FromCompany", ns).setText("Publications Office"));
        //header.addContent(new Element("FromPerson", ns).setText(""));
        header.addContent(new Element("FromEmail", ns).setText("OP-ODP-CONTACT@publications.europa.eu"));
        header.addContent(new Element("ToCompany", ns).setText("OP"));
        header.addContent(new Element("SentDate", ns).setText(new SimpleDateFormat("yyyyMMdd").format(new Date())));
        root.addContent(header);

        Element dOIData = new Element("DOIData", ns);
        // Put the DOI here
        dOIData.addContent(new Element("DOI", ns).setText(doi));
        // Put the actual URI here
        dOIData.addContent(new Element("DOIWebsiteLink", ns).setText(datasetURL));

        Element metadata = new Element("Metadata", ns);

        Namespace nsDatacite =  Namespace.getNamespace("http://datacite.org/schema/kernel-4");
        Element resource = new Element("resource", nsDatacite);

        if(dataset.containsKey("creator")) {
            resource.addContent(
                    new Element("creators", nsDatacite)
                            .addContent(new Element("creator", nsDatacite)
                                    .addContent(new Element("creatorName", nsDatacite)
                                            .setAttribute("nameType", "Organizational")
                                            .setText(dataset.getJsonObject("creator").getString("name"))))); // Set creator here
        } else {
            throw new IdentifierException("The dataset has no creator");
        }

        // Set the publisher
        if(dataset.containsKey("publisher")) {
            resource.addContent(new Element("publisher", nsDatacite).setText(dataset.getJsonObject("publisher").getString("name")));
        } else {
            throw new IdentifierException("The dataset has no publisher");
        }

        // Set DOI here again
        resource.addContent(new Element("identifier", nsDatacite).setAttribute("identifierType", "DOI").setText(doi));

        // Add title here
        Element titles = new Element("titles", nsDatacite);
        dataset.getJsonObject("title").forEach(title -> {
            titles.addContent(new Element("title", nsDatacite)
                    .setAttribute("lang", title.getKey(), Namespace.XML_NAMESPACE)
                    .setText(title.getValue().toString()));
        });
        resource.addContent(titles);

        // Set publication date
        if(dataset.containsKey("issued")) {
            String year;
            if (isDateTime(dataset.getString("issued"))) {
                year = String.valueOf(ZonedDateTime.parse(dataset.getString("issued")).getYear());
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                sdf.setLenient(false);
                try {
                    year = sdf.format(sdf.parse(dataset.getString("issued")));
                } catch (Exception ex) {
                    throw new IdentifierException(ex.getMessage());
                }
            }
            resource.addContent(new Element("publicationYear", nsDatacite).setText(year));
        } else {
            throw new IdentifierException("The dataset has no publication year");
        }

        resource.addContent(new Element("resourceType",nsDatacite).setAttribute("resourceTypeGeneral", "Dataset").setText("Dataset"));
        // Set subjects
        Element subjects = new Element("subjects", nsDatacite);
        // Set EuroVoc here
        if(dataset.containsKey("subject")) {
            dataset.getJsonArray("subject").forEach(subject -> {
                if(subject instanceof JsonObject) {
                    subjects.addContent(new Element("subject", nsDatacite)
                            .setAttribute("schemeURI", "http://eurovoc.europa.eu/")
                            .setAttribute("subjectScheme", "EuroVoc")
                            .setText(((JsonObject) subject).getString("label")));

                }
            });
        }
        // Set Keywords here
        if(dataset.containsKey("keywords")) {
            dataset.getJsonArray("keywords").forEach(keyword -> {
                if(keyword instanceof JsonObject) {
                    subjects.addContent(new Element("subject", nsDatacite)
                            .setAttribute("lang", ((JsonObject) keyword).getString("language"), Namespace.XML_NAMESPACE)
                            .setText(((JsonObject) keyword).getString("label")));
                }

            });
        }

        resource.addContent(subjects);

        // Add Date here
        if(dataset.containsKey("issued")) {
            String issuedDate;
            if (isDateTime(dataset.getString("issued"))) {
                issuedDate = ZonedDateTime.parse(dataset.getString("issued")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                try {
                    SimpleDateFormat issuedDataFormat = new SimpleDateFormat("yyyy/MM/dd");
                    issuedDate = issuedDataFormat.format(sdf.parse(dataset.getString("issued")));
                } catch (Exception ex) {
                    throw new IdentifierException(ex.getMessage());
                }
            }
            resource.addContent(new Element("dates", nsDatacite)
                    .addContent(new Element("date", nsDatacite)
                            .setAttribute("dateType", "Issued")
                            .setText(issuedDate)));
        }

        // Add description here
        Element descriptions = new Element("descriptions", nsDatacite);
        if(dataset.containsKey("description")) {
            dataset.getJsonObject("description").forEach(description -> {
                descriptions.addContent(new Element("description", nsDatacite)
                        .setAttribute("descriptionType", "Abstract")
                        .setAttribute("lang", description.getKey(), Namespace.XML_NAMESPACE)
                        .setText(description.getValue().toString()));
            });
        }
        resource.addContent(descriptions);

        // ToDo How to find the actual language?
        resource.addContent(new Element("language", nsDatacite).setText("en"));

        // Add Documentation here
        // ToDo relatedIdentifiers
//        resource.addContent(new Element("relatedIdentifiers", nsDatacite)
//                .addContent(new Element("relatedIdentifier", nsDatacite)
//                        .setAttribute("relatedIdentifierType", "PURL")
//                        .setAttribute("relationType", "Documents")
//                        .setText("http://data.europa.eu/88u/dataset/national-interoperability-framework-observatory-nifo-digital-government-factsheets-2020")));

        // Add actual URL of the dataset here
        resource.addContent(new Element("alternateIdentifiers", nsDatacite)
                .addContent(new Element("alternateIdentifier", nsDatacite)
                        .setAttribute("alternateIdentifierType", "PURL")
                        .setText(datasetURL)));

        metadata.addContent(resource);
        dOIData.addContent(metadata);
        root.addContent(dOIData);
        document.setContent(root);

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        return outputter.outputString(document);
    }

    @Override
    public JsonObject getIdentifierEligibility(JsonObject metadata) {
        boolean isCreatorNameExist = metadata.containsKey("creator") && metadata.getJsonObject("creator").getString("name") != null;
        boolean isPublisherNameExist = metadata.containsKey("publisher") && metadata.getJsonObject("publisher").getString("name") != null;
        boolean isPublicationDateExist = metadata.containsKey("issued");

        JsonObject requiredElement = new JsonObject();
        requiredElement
                .put("creator", isCreatorNameExist)
                .put("publisher", isPublisherNameExist)
                .put("issued", isPublicationDateExist);

        JsonObject result = new JsonObject();
        result.put("identifierName", getName())
                .put("isEligible", isCreatorNameExist && isPublisherNameExist && isPublicationDateExist)
                .put("element", requiredElement);

        return result;
    }

    private boolean isDateTime(String dateString) {
        try {
            ZonedDateTime.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
