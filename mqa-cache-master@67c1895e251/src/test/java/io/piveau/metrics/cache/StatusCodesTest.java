package io.piveau.metrics.cache;

import io.piveau.metrics.cache.dqv.StatusCodes;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatusCodesTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    @DisplayName("Test Status Codes to JSON Object")
    void statusCodesToJsonObject(Vertx vertx, VertxTestContext testContext) {
        Map<String, Double> testMap = new HashMap<>();
        testMap.put("200", 2.0);
        testMap.put("test", 33.0);
        StatusCodes statusCodes = new StatusCodes(testMap);
        JsonObject check = new JsonObject()
                .put("200", 2.0)
                .put("test", 33.0);
        testContext.verify(() -> {
            assertEquals(check, statusCodes.toJson());
        });
        testContext.completeNow();
    }

    @Test
    @DisplayName("Test Status Codes to JSON Array")
    void statusCodesToJsonArray(Vertx vertx, VertxTestContext testContext) {
        Map<String, Double> testMap = new HashMap<>();
        testMap.put("200", 2.0);
        StatusCodes statusCodes = new StatusCodes(testMap);
        JsonArray check = new JsonArray()
                .add(new JsonObject()
                        .put("name", "200")
                        .put("percentage", 2.0));
        testContext.verify(() -> {
            assertEquals(check, statusCodes.toJsonArray());
        });
        testContext.completeNow();
    }

}
