package io.piveau.hub;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.services.identifiers.EURADOIRegistry;
import io.piveau.hub.services.identifiers.Identifier;
import io.piveau.hub.services.identifiers.MockRegistry;
import io.piveau.hub.indexing.Indexing;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.vocabularies.vocabulary.ADMS;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Testing Identifiers Features")
@ExtendWith(VertxExtension.class)
public class IdentifiersTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentifiersTest.class);

    Resource mockDataset;
    String mockDatasetURI;
    WebClient webClient;
    DatasetHelper datasetHelper;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        Model model = ModelFactory.createDefaultModel();
        mockDatasetURI = "http://mock.io/dataset/1234";
        mockDataset = model.createResource(mockDatasetURI);
        mockDataset.addProperty(DCTerms.title, "title");
        webClient = WebClient.create(vertx);
        Buffer buffer = vertx.fileSystem().readFileBlocking("example_dataset.ttl");
        DatasetHelper.create(buffer.toString(), RDFMimeTypes.TURTLE, ar -> {
            if(ar.succeeded()) {
                datasetHelper = ar.result();
                testContext.completeNow();
            } else {
                testContext.failNow("Could not create DatasetHelper");
            }
        });
    }

    @Test
    @DisplayName("Test the Identifier Mock Registry")
    void testMockRegistry(Vertx vertx, VertxTestContext testContext) {
        JsonObject config = new JsonObject();
        MockRegistry mockRegistry = new MockRegistry(vertx, webClient, config);
        mockRegistry.getIdentifier(mockDatasetURI, datasetHelper, new ArrayList<>()).onSuccess(ar -> {
            assertEquals(ar.creatorURI, "http://piveau.eu/creator");
            testContext.completeNow();
        }).onFailure(ar -> {
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Check if the Mock Registry's requirement is fulfilled")
    void testMockRegistryEligibility(Vertx vertx, VertxTestContext testContext) {
        JsonObject config = new JsonObject();
        MockRegistry mockRegistry = new MockRegistry(vertx, webClient, config);

        JsonObject metadata = Indexing.indexingDataset(datasetHelper.resource(), datasetHelper.recordResource(), null, "en");

        JsonObject requiredElement = new JsonObject();
        requiredElement.put("none", true);
        JsonObject expectedResult = new JsonObject();
        expectedResult.put("identifierName", "Mock Identity Registry")
                .put("isEligible", true)
                .put("element", requiredElement);

        JsonObject actualResult = mockRegistry.getIdentifierEligibility(metadata);
        testContext.verify(() -> Assertions.assertEquals(expectedResult, actualResult));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Test the Identifier EURADOIRegistry")
    @Disabled
    void testEURADOIRegistry(Vertx vertx, VertxTestContext testContext) {
        JsonObject config = new JsonObject();
        config.put("prefix", "xx.xxxx");
        config.put("type", "eu-ra-doi");
        config.put("enabled", true);
        config.put("api", "https://xxxxx");
        config.put("user", "xxxxx");
        config.put("password", "xxxxxxx");

        EURADOIRegistry euradoiRegistry = new EURADOIRegistry(vertx, webClient, config);

        List<Identifier> identifiers = new ArrayList<>();
        datasetHelper.resource().listProperties(ADMS.identifier).forEach(statement -> {
            Resource res = statement.getResource();
            if(res.isURIResource()) {
                Identifier identifier = new Identifier();
                identifier.identifierURI = res.getURI();
                if(res.hasProperty(SKOS.notation)) {
                    identifier.identifier = res.getProperty(SKOS.notation).getString();
                    identifier.schema = res.getProperty(SKOS.notation).getLiteral().getDatatypeURI();
                }
                identifiers.add(identifier);
            }
        });

        euradoiRegistry.getIdentifier(datasetHelper.uriRef(), datasetHelper, identifiers).onSuccess(ar -> {
            testContext.verify(() -> {
                assertNotNull(ar.toString());
            });
            logger.info(ar.toString());
            testContext.completeNow();
        }).onFailure(ar -> {
            logger.error(ar.getMessage());
            testContext.failNow(ar.getCause());
        });
    }

    @Test
    @DisplayName("Test the request XML and DOI registration")
    void testEURADOIRegistryRequestXML(Vertx vertx, VertxTestContext testContext) throws NoSuchMethodException {
        JsonObject config = new JsonObject();
        EURADOIRegistry euradoiRegistry = new EURADOIRegistry(vertx, webClient, config);
        EURADOIRegistry euradoiRegistryMock = mock(euradoiRegistry.getClass());

        // Set EURADOIRegistry.buildRequestXML method accessible
        Method buildRequestXMLMethod = euradoiRegistry.getClass().getDeclaredMethod("buildRequestXML", String.class, String.class, JsonObject.class);
        buildRequestXMLMethod.setAccessible(true);

        try {
            JsonObject metadata = Indexing.indexingDataset(datasetHelper.resource(), datasetHelper.recordResource(), null, "en");
            // Build a request XML with a dummy DOI
            String requestXML = (String) buildRequestXMLMethod.invoke(euradoiRegistry, "xx.xxxx/469379971390968", datasetHelper.uriRef(), metadata);

            // Check Header and DOIData elements existence in the request XML
            String nPrefix = "publication";
            InputSource inputXML = new InputSource(new StringReader(requestXML));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            Document doc = builderFactory.newDocumentBuilder().parse(inputXML);
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                @Override
                public String getNamespaceURI(String prefix) {
                    if (prefix == null) throw new NullPointerException("NULL prefix");
                    else if (nPrefix.equals(prefix)) return "http://ra.publications.europa.eu/schema/doidata/1.0";
                    return XMLConstants.NULL_NS_URI;
                }
                @Override
                public String getPrefix(String namespaceURI) {
                    return null;
                }
                @Override
                public Iterator<String> getPrefixes(String namespaceURI) {
                    return null;
                }
            });
            Node nodeHeader = (Node) xPath.compile("/"+nPrefix+":DOIRegistrationMessage/"+nPrefix+":Header")
                    .evaluate(doc, XPathConstants.NODE);
            Node nodeDOIData = (Node) xPath.compile("/"+nPrefix+":DOIRegistrationMessage/"+nPrefix+":DOIData")
                    .evaluate(doc, XPathConstants.NODE);
            boolean isHeaderAndDOIDataElementExist = nodeHeader != null && nodeDOIData != null;

            // Mock EURADOIRegistry.registerDOI(requestXML)
            Promise<JsonObject> dummyResult = Promise.promise();
            dummyResult.complete(new JsonObject());
            when(euradoiRegistryMock.registerDOI(requestXML)).thenReturn(dummyResult.future());
            Future<JsonObject> mockResult = euradoiRegistryMock.registerDOI(requestXML);

            testContext.verify(() -> {
                Assertions.assertTrue(isHeaderAndDOIDataElementExist);
                Assertions.assertEquals(dummyResult, mockResult);
            });
            testContext.completeNow();
        } catch (Exception e) {
            logger.error(e.getMessage());
            testContext.failNow(e.getMessage());
        } finally {
            buildRequestXMLMethod.setAccessible(false);
        }
    }

    @Test
    @DisplayName("Check if the EURADOIRegistry's requirement is fulfilled")
    void testEURADOIRegistryEligibility(Vertx vertx, VertxTestContext testContext) {
        JsonObject config = new JsonObject();
        EURADOIRegistry euradoiRegistry = new EURADOIRegistry(vertx, webClient, config);

        JsonObject metadata = Indexing.indexingDataset(datasetHelper.resource(), datasetHelper.recordResource(), null, "en");
        boolean isCreatorNameExist = metadata.containsKey("creator") && metadata.getJsonObject("creator").getString("name") != null;
        boolean isPublisherNameExist = metadata.containsKey("publisher") && metadata.getJsonObject("publisher").getString("name") != null;
        boolean isPublicationDateExist = metadata.containsKey("issued");

        JsonObject requiredElement = new JsonObject();
        requiredElement
                .put("creator", isCreatorNameExist)
                .put("publisher", isPublisherNameExist)
                .put("issued", isPublicationDateExist);
        JsonObject expectedResult = new JsonObject();
        expectedResult.put("identifierName", "EU RA DOI Registry")
                .put("isEligible", isCreatorNameExist && isPublisherNameExist && isPublicationDateExist)
                .put("element", requiredElement);

        JsonObject actualResult = euradoiRegistry.getIdentifierEligibility(metadata);
        testContext.verify(() -> Assertions.assertEquals(expectedResult, actualResult));
        testContext.completeNow();
    }
}
