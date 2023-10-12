package io.piveau.hub.search.UnitTest;

import io.piveau.hub.search.util.date.DateChecker;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.joda.time.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing the DateChecker")
@ExtendWith(VertxExtension.class)
class DateCheckerTest {

    @Test
    @DisplayName("Testing checkDates with correct payload")
    void testCheckDatesWithCorrectPayload(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload_before = new JsonObject()
                .put("id", "test-id")
                .put("modified", new DateTime().toString())
                .put("issued", new DateTime().toString());

        JsonObject payload_after = new JsonObject(payload_before.toString());

        DateChecker.check(payload_after);
        assertEquals(payload_before, payload_after);
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing checkDates with modified in future")
    void testCheckDatesFutureModificationDate(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload_before = new JsonObject()
                .put("id", "test-id")
                .put("modified", new DateTime().plusYears(1).toString())
                .put("issued", new DateTime().toString());

        JsonObject payload_after = new JsonObject(payload_before.toString());

        DateChecker.check(payload_after);
        assertEquals("_" + payload_before.getString("modified"),
                payload_after.getString("modified"));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing checkDates with issued in future")
    void testCheckDatesFutureReleaseDate(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload_before = new JsonObject()
                .put("id", "test-id")
                .put("modified", new DateTime().toString())
                .put("issued", new DateTime().plusYears(1).toString());

        JsonObject payload_after = new JsonObject(payload_before.toString());

        DateChecker.check(payload_after);
        assertEquals("_" + payload_before.getString("issued"),
                payload_after.getString("issued"));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing checkDates with modified and issued in future")
    void testCheckDatesFutureModificationDateAndFutureReleaseDate(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload_before = new JsonObject()
                .put("id", "test-id")
                .put("modified", new DateTime().plusYears(1).toString())
                .put("issued", new DateTime().plusYears(1).toString());

        JsonObject payload_after = new JsonObject(payload_before.toString());

        DateChecker.check(payload_after);
        assertEquals("_" + payload_before.getString("modified"),
                payload_after.getString("modified"));
        assertEquals("_" + payload_before.getString("issued"),
                payload_after.getString("issued"));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing checkDates with correct payload array")
    void testCheckDatesWithCorrectPayloadArray(Vertx vertx, VertxTestContext testContext) {
        JsonArray payload_array_before = new JsonArray()
                .add(new JsonObject()
                        .put("id", "test-id-1")
                        .put("modified", new DateTime().toString())
                        .put("issued", new DateTime().toString()))
                .add(new JsonObject()
                        .put("id", "test-id-2")
                        .put("modified", new DateTime().toString())
                        .put("issued", new DateTime().toString()));

        JsonArray payload_array_after = new JsonArray(payload_array_before.toString());

        DateChecker.check(payload_array_after);
        assertEquals(payload_array_before, payload_array_after);
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing checkDates with permutating modified and issued in future")
    void testCheckDatesWithPermutatingPayloadArray(Vertx vertx, VertxTestContext testContext) {
        JsonArray payload_array_before = new JsonArray()
                .add(new JsonObject()
                        .put("id", "test-id-1")
                        .put("modified", new DateTime().toString())
                        .put("issued", new DateTime().toString()))
                .add(new JsonObject()
                        .put("id", "test-id-2")
                        .put("modified", new DateTime().plusYears(1).toString())
                        .put("issued", new DateTime().toString()))
                .add(new JsonObject()
                        .put("id", "test-id-3")
                        .put("modified", new DateTime().toString())
                        .put("issued", new DateTime().plusYears(1).toString()))
                .add(new JsonObject()
                        .put("id", "test-id-4")
                        .put("modified", new DateTime().plusYears(1).toString())
                        .put("issued", new DateTime().plusYears(1).toString()));

        JsonArray payload_array_after = new JsonArray(payload_array_before.toString());

        DateChecker.check(payload_array_after);
        for (int i = 0; i < 4; ++i) {
            if ((i & 1) == 1) {
                assertEquals("_" + payload_array_before.getJsonObject(i).getString("modified"),
                        payload_array_after.getJsonObject(i).getString("modified"));
            } else {
                assertEquals(payload_array_before.getJsonObject(i).getString("modified"),
                        payload_array_after.getJsonObject(i).getString("modified"));
            }

            if ((i & 2) == 2) {
                assertEquals("_" + payload_array_before.getJsonObject(i).getString("issued"),
                        payload_array_after.getJsonObject(i).getString("issued"));
            } else {
                assertEquals(payload_array_before.getJsonObject(i).getString("issued"),
                        payload_array_after.getJsonObject(i).getString("issued"));
            }
        }

        testContext.completeNow();
    }

}