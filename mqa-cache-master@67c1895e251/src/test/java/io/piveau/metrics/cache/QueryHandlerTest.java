package io.piveau.metrics.cache;

import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryHandlerTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeAll
    void init(Vertx vertx, VertxTestContext testContext) {
        QueryCollection.init(vertx, "testQueries");
        testContext.completeNow();
    }

    @Test
    @DisplayName("List queries")
    void listQuery(Vertx vertx, VertxTestContext testContext) {
        String query = QueryCollection.getQuery("Test2Query");
        log.debug(String.format(query, "string1", "string2", 100, 30));
        testContext.completeNow();
    }

}
