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

import java.util.Set;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryCollectionTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeAll
    void init(Vertx vertx, VertxTestContext testContext) {
        QueryCollection.init(vertx, "testQueries");
        testContext.completeNow();
    }

    @Test
    @DisplayName("List queries")
    void listQuery(Vertx vertx, VertxTestContext testContext) {
        Set<String> queries = QueryCollection.listQueries();
        log.debug(queries.toString());
        testContext.verify(() -> {
            assert queries.size() == 2;
        });
        testContext.completeNow();
    }

    @Test
    @DisplayName("List queries")
    void listResources(Vertx vertx, VertxTestContext testContext) {
        String query = QueryCollection.getQuery("TestQuery");
        testContext.verify(() -> {
            log.debug(query);
            assert !query.isBlank();
        });
        testContext.completeNow();
    }

}
