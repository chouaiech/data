package io.piveau.hub.search.UnitTest;

import io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch.SearchResponseHelper;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing the SearchResponseHelper")
@ExtendWith(VertxExtension.class)
class SearchResponseHelperTest {

    private final Logger LOG = LoggerFactory.getLogger(SearchResponseHelperTest.class);

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        testContext.completeNow();
    }

    @AfterEach
    void tearDown(Vertx vertx, VertxTestContext testContext) {
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing basic processSearchResult with correct modified and issued")
    void testProcessSearchResultWithCorrectDates(Vertx vertx, VertxTestContext testContext) {
        try {
            // TODO: Check how to set index correctly
            String modified = "_" + DateTime.now().plusYears(1).toString();
            String issued = "_" + DateTime.now().plusYears(1).toString();

            SearchHit[] hits = new SearchHit[1];

            BytesReference source1 = new BytesArray("{\n" +
                    "                \"id\" : \"test-id-1\",\n" +
                    "                \"modified\" : \"" + modified +"\",\n" +
                    "                \"issued\" : \"" + issued +"\"\n" +
                    "            }");

            Map<String, DocumentField> fields = new HashMap<>();

            DocumentField ignored = new DocumentField("_ignored", new ArrayList<>());

            ignored.getValues().add("modified");
            ignored.getValues().add("issued");

            fields.put("_ignored", ignored);

            hits[0] = new SearchHit(1, "test-id-1", new Text("_doc"), fields, null).sourceRef(source1);

            JsonArray results = SearchResponseHelper
                    .processSearchResult(hits,null, null, null, null, null);

            JsonObject result = results.getJsonObject(0);

            assertEquals(modified.substring(1), result.getString("modified"));
            assertEquals(issued.substring(1), result.getString("issued"));

            testContext.completeNow();
        } catch (Exception e) {
            testContext.failNow(e);
        }
    }

    @Test
    @DisplayName("Testing basic processSearchResult with modified and issued in future")
    void testProcessSearchResultWithFutureDates(Vertx vertx, VertxTestContext testContext) {
        try {
            // TODO: Check how to set index correctly
            String modified = DateTime.now().toString();
            String issued = DateTime.now().toString();

            SearchHit[] hits = new SearchHit[1];

            BytesReference source1 = new BytesArray("{\n" +
                    "                \"id\" : \"test-id-1\",\n" +
                    "                \"modified\" : \"" + modified +"\",\n" +
                    "                \"issued\" : \"" + issued +"\"\n" +
                    "            }");

            hits[0] = new SearchHit(1, "test-id-1", new Text("_doc"), null, null).sourceRef(source1);

            JsonArray results = SearchResponseHelper
                    .processSearchResult(hits,null, null, null, null, null);

            JsonObject result = results.getJsonObject(0);

            assertEquals(modified, result.getString("modified"));
            assertEquals(issued, result.getString("issued"));

            testContext.completeNow();
        } catch (Exception e) {
            testContext.failNow(e);
        }
    }

    @Test
    @DisplayName("Testing basic processSearchResult with malformed modified and issued")
    void testProcessSearchResultWithMalformedDates(Vertx vertx, VertxTestContext testContext) {
        try {
            // TODO: Check how to set index correctly
            String modified = "malformed_modified";
            String issued = "malformed_issued";

            SearchHit[] hits = new SearchHit[1];

            BytesReference source1 = new BytesArray("{\n" +
                    "                \"id\" : \"test-id-1\",\n" +
                    "                \"modified\" : \"" + modified +"\",\n" +
                    "                \"issued\" : \"" + issued +"\"\n" +
                    "            }");

            Map<String, DocumentField> fields = new HashMap<>();

            DocumentField ignored = new DocumentField("_ignored", new ArrayList<>());

            ignored.getValues().add("modified");
            ignored.getValues().add("issued");

            fields.put("_ignored", ignored);

            hits[0] = new SearchHit(1, "test-id-1", new Text("_doc"), fields, null).sourceRef(source1);

            JsonArray results = SearchResponseHelper
                    .processSearchResult(hits,null, null, null, null, null);

            JsonObject result = results.getJsonObject(0);

            assertEquals(modified, result.getString("modified"));
            assertEquals(issued, result.getString("issued"));

            testContext.completeNow();
        } catch (Exception e) {
            testContext.failNow(e);
        }
    }
}