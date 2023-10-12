package io.piveau.metrics.reporter.pdf;

import io.piveau.metrics.reporter.CatalogueMetrics;
import io.piveau.metrics.reporter.model.Translation;
import io.piveau.metrics.reporter.model.chart.BarChart;
import io.piveau.metrics.reporter.model.chart.DoughnutChart;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class PdfGeneratorTest {

    private Translation translation;

    private PdfReportBuilder builder;

    @BeforeAll
    void setUp(Vertx vertx, VertxTestContext testContext) {

        JsonObject config = new JsonObject()
                .put("portalUrl", "data.europa.eu")
                .put("shaclUrl", "data.europa.eu/shacl")
                .put("methodologyUrl", "data.europa.eu/mqa")
                .put("headerFilename", "report_header.png");

        builder = new PdfReportBuilder(vertx, config);

        JsonObject i18n = vertx.fileSystem().readFileBlocking("i18n/lang.json").toJsonObject();
        translation = Translator.createTranslation(i18n.getJsonObject(Locale.ENGLISH.getLanguage().toLowerCase()), Locale.ENGLISH);

        DoughnutChart.template = vertx.fileSystem().readFileBlocking("charts/doughnut.json").toJsonObject();
        BarChart.template = vertx.fileSystem().readFileBlocking("charts/bar.json").toJsonObject();

        testContext.completeNow();
    }

    @Test
    void generatePdf(Vertx vertx, VertxTestContext testContext) {
        long start = System.currentTimeMillis();
        PdfGenerator generator = builder.createGenerator();

        JsonObject govdataData = vertx.fileSystem().readFileBlocking("govdata.json").toJsonObject();
        CatalogueMetrics catalogueMetrics = new CatalogueMetrics(govdataData, translation);

        WebClient client = WebClient.create(vertx);

        List<Future<HttpResponse<Buffer>>> futures = new ArrayList<>();

        catalogueMetrics.getDimensionCharts()
                .forEach(chart -> {
                    futures.add(client.postAbs("http://localhost:3400/chart")
                            .expect(ResponsePredicate.SC_OK)
                            .sendJsonObject(chart.getChart())
                            .onSuccess(response -> {
                                chart.setImageBytes(response.body().getBytes());
                            }));
                });

        CompositeFuture.all(new ArrayList<>(futures)).onComplete(f -> {
            generator.render(catalogueMetrics, translation);
            generator.save("TestPdf.pdf");

            System.out.println(System.currentTimeMillis() - start);

            testContext.completeNow();
        });
    }

}
