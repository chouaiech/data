package io.piveau.metrics.reporter.pdf;

import io.piveau.metrics.reporter.CatalogueMetrics;
import io.piveau.metrics.reporter.model.Catalogue;
import io.piveau.metrics.reporter.model.MetricSection;
import io.piveau.metrics.reporter.model.ReportTask;
import io.piveau.metrics.reporter.model.Translation;
import io.piveau.metrics.reporter.model.chart.BarChart;
import io.piveau.metrics.reporter.model.chart.Chart;
import io.piveau.metrics.reporter.model.chart.DoughnutChart;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static io.piveau.metrics.reporter.ApplicationConfig.*;

public class PdfReportVerticle extends AbstractVerticle {

    public static final String CATALOGUE_ADDRESS = "generate.catalogue.report.pdf";
    public static final String GLOBAL_ADDRESS = "generate.global.report.pdf";

    public static final String ADDRESS = "pdf-report-generator";

    private static final Logger log = LoggerFactory.getLogger(PdfReportVerticle.class);

    private WebClient client;

    private JsonObject countries;

    private JsonObject i18n;

    private Styles styles;

    Map<String, Translation> translations = new HashMap<>();

    private PdfReportBuilder reportBuilder;

    private String metricsAddress;
    private String quickchartAddress;

    @Override
    public void start(Promise<Void> startPromise) {
        client = WebClient.create(vertx);
        vertx.eventBus().consumer(ADDRESS, this::generateReports);

        vertx.eventBus().consumer(CATALOGUE_ADDRESS, this::generateCatalogueReport);

        List<Future> readFiles = new ArrayList<>();

        Future<Buffer> readDoughnut = readFileFromResources("charts/doughnut.json");
        readFiles.add(readDoughnut);

        Future<Buffer> readBar = readFileFromResources("charts/bar.json");
        readFiles.add(readBar);

        readFiles.add(
                readFileFromResources("i18n/lang.json")
                        .map(Buffer::toJsonObject)
                        .onSuccess(i18n -> {
                            this.i18n = i18n;
                            i18n.stream()
                                    .map(entry -> Map.entry(entry.getKey(), (JsonObject) entry.getValue()))
                                    .forEach(entry -> {
                                        translations.put(entry.getKey(), Translator.createTranslation(entry.getValue(), Locale.forLanguageTag(entry.getKey())));
                                    });
                        })
        );

        reportBuilder = new PdfReportBuilder(vertx, new JsonObject());

        metricsAddress = config().getString("METRICS_ADDRESS", "http://piveau-metrics-cache:8080");
        quickchartAddress = config().getString("QUICKCHART_ADDRESS", "http://piveau-metrics-quickchart:3400");

        Future<Buffer> readCountries = readFileFromResources("i18n/countries.json");
        readFiles.add(readCountries);

        CompositeFuture.all(readFiles).onSuccess(success -> {
            try {
                DoughnutChart.template = readDoughnut.result().toJsonObject();
                BarChart.template = readBar.result().toJsonObject();
                countries = readCountries.result().toJsonObject();

                TTFParser parser = new TTFParser(true);

                Buffer buffer = vertx.fileSystem().readFileBlocking("fonts/" + config().getString(ENV_FONT_REGULAR, DEFAULT_FONT_REGULAR));
                TrueTypeFont robotoRegularTTFFile = parser.parseEmbedded(new ByteArrayInputStream(buffer.getBytes()));

                buffer = vertx.fileSystem().readFileBlocking("fonts/" + config().getString(ENV_FONT_BOLD, DEFAULT_FONT_BOLD));
                TrueTypeFont robotoBoldTTFFile = parser.parseEmbedded(new ByteArrayInputStream(buffer.getBytes()));

                buffer = vertx.fileSystem().readFileBlocking("fonts/" + config().getString(ENV_FONT_ITALIC, DEFAULT_FONT_ITALIC));
                TrueTypeFont robotoItalicTTFFile = parser.parseEmbedded(new ByteArrayInputStream(buffer.getBytes()));

                buffer = vertx.fileSystem().readFileBlocking("fonts/" + config().getString(ENV_FONT_BOLD_ITALIC, DEFAULT_FONT_BOLD_ITALIC));
                TrueTypeFont robotoBoldItalicTTFFile = parser.parseEmbedded(new ByteArrayInputStream(buffer.getBytes()));

                styles = new Styles(
                        robotoRegularTTFFile,
                        robotoBoldTTFFile,
                        robotoItalicTTFFile,
                        robotoBoldItalicTTFFile);

                startPromise.complete();
            } catch (IOException e) {
                startPromise.fail(e);
            }
        }).onFailure(startPromise::fail);
    }

    /*
     * Method returns a Catalogue with Section objects which include chart images and their headings for a specific section (dimension)
     */
    private Future<Catalogue> createCataloguePdf(JsonObject catalogueData, ReportTask task) {
        return Future.future(promise -> {

            Translator translator = new Translator(task.getLanguageCode(), i18n, countries);

            Catalogue catalogue = new Catalogue(catalogueData);
            catalogue.generateMetricSections(translator);

            // accommodate to 31 char limit in excel sheets, which are used for generating tabular file names
            String catalogueReportFile = task.getPath()
                    + catalogue.getId()
                    + ".pdf";

            String catalogueTmpFile = catalogueReportFile + ".tmp";

            try {
                PdfReport pdfReport = new PdfReport(task, styles, translator, vertx, config());

                List<Future<Void>> sections = catalogue.getSections().stream()
                        .map(this::saveChartImagesToSection)
                        .collect(Collectors.toList());

                pdfReport.addTitlePage();

                CompositeFuture.join(new ArrayList<>(sections))
                        .compose(v -> pdfReport.addMethodologyPage())
                        .compose(v -> pdfReport.addCatalogueDataToDocument(catalogue))
                        .compose(v -> pdfReport.save(catalogueTmpFile))
                        .onSuccess(success ->
                                renameTmpFile(catalogueTmpFile, catalogueReportFile, promise, catalogue)
                        ).onFailure(promise::fail);

            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    private Future<Void> createGlobalReport(ReportTask task, Translator translator, List<Catalogue> completedCatalogues) {
        return Future.future(promise -> {
            String globalReportFileName = task.getPath() + "global.pdf";
            String globalReportTmpFileName = globalReportFileName + ".tmp";

            try {
                PdfReport pdfReport = new PdfReport(task, styles, translator, vertx, config());
                pdfReport.addTitlePage();

                pdfReport.addMethodologyPage()
                        .compose(v -> pdfReport.addNavigationDashboard())
                        .compose(v -> pdfReport.addAllCatalogueDataToDocument(completedCatalogues))
                        .compose(v -> pdfReport.save(globalReportTmpFileName))
                        .onSuccess(success -> renameTmpFile(globalReportTmpFileName, globalReportFileName, promise, null))
                        .onFailure(promise::fail);
            } catch (Exception e) {
                promise.fail(e);
            }
        });
    }

    private Future<Void> saveChartImagesToSection(MetricSection section) {
        return Future.future(promise -> {
            ArrayList<Future> futureImageList = new ArrayList<>();

            for (Chart chart : section.getCharts()) {
                JsonObject chartTemplate = chart.getChart();

                Promise<Void> imagePromise = Promise.promise();
                futureImageList.add(imagePromise.future());

                if (chartTemplate == null) {
                    imagePromise.complete();
                } else {
                    client.postAbs(config().getString(ENV_QUICKCHART_ADDRESS))
                            .expect(ResponsePredicate.SC_OK)
                            .sendJsonObject(chartTemplate, ar -> {
                                if (ar.succeeded()) {
                                    // Obtain response from Quickchart API
                                    HttpResponse<Buffer> response = ar.result();
                                    chart.setImageBytes(response.body().getBytes());

                                    imagePromise.complete();
                                } else {
                                    imagePromise.fail(ar.cause());
                                }
                            });
                }
            }

            CompositeFuture.all(futureImageList)
                    .onSuccess(success -> promise.complete())
                    .onFailure(promise::fail);
        });
    }

    private Future<Buffer> readFileFromResources(String fileName) {
        return vertx.fileSystem().readFile(fileName);
    }

    private void renameTmpFile(String replacement, String fileToReplace, Promise handler, Catalogue result) {
        vertx.fileSystem().move(replacement,
                fileToReplace,
                new CopyOptions().setReplaceExisting(true),
                replaceFile -> {
                    if (replaceFile.succeeded()) {
                        handler.complete(result);
                    } else {
                        log.error("Failed to replace [{}] with [{}], cleaning up...", fileToReplace, replacement, replaceFile.cause());
                        vertx.fileSystem().delete(replacement, deleteFile ->
                        {
                            if (deleteFile.failed()) {
                                log.error("Failed to clean up temporary file [{}]", replacement, deleteFile.cause());
                            }
                            handler.fail("PDF file could not be moved");
                        });
                    }
                });
    }

    //private void generateReports(Message<JsonObject> message) {
    private void generateReports(Message<String> message) {
        //ReportTask task = message.body().mapTo(ReportTask.class);
        JsonObject received = new JsonObject(message.body());
        ReportTask task = new ReportTask(
                new Locale(received.getString("languageCode")),
                received.getJsonArray("metrics").stream()
                        .map(entry -> (JsonObject) entry)
                        .collect(Collectors.toList())
        );

        List<Future<Catalogue>> list = task.getMetrics().stream()
                .map(catalogue -> createCataloguePdf(catalogue, task))
                .collect(Collectors.toList());

        CompositeFuture.join(new ArrayList<>(list))
                .onSuccess(success ->
                        createGlobalReport(task, new Translator(task.getLanguageCode(), i18n, countries), success.list())
                                .onSuccess(successGlobalReport ->
                                        log.info("Generated PDF reports for language [{}] done.", task.getLanguageCode()))
                                .onFailure(failure ->
                                        log.error("Failed to generate global PDF report for language [{}]", task.getLanguageCode(), failure)))
                .onFailure(failure -> log.error("Failed to generate PDF reports for language [{}]", task.getLanguageCode(), failure));
    }

    private void generateCatalogueReport(Message<JsonObject> message) {
        String catalogueId = message.body().getString("catalogueId");
        String languageCode = message.body().getString("languageCode");

        Translation translation = translations.get(languageCode);

        getCatalogueMetrics(catalogueId)
                .onSuccess(metrics -> {
                    CatalogueMetrics catalogueMetrics = new CatalogueMetrics(metrics, translation);

                    List<Future<HttpResponse<Buffer>>> futures = new ArrayList<>();

                    catalogueMetrics.getDimensionCharts()
                            .forEach(chart -> {
                                futures.add(client.postAbs(quickchartAddress)
                                        .expect(ResponsePredicate.SC_OK)
                                        .sendJsonObject(chart.getChart())
                                        .onSuccess(response -> {
                                            chart.setImageBytes(response.body().getBytes());
                                        })
                                        .onFailure(cause -> log.error("Render chart", cause))
                                );
                            });

                    CompositeFuture.all(new ArrayList<>(futures)).onComplete(f -> {
                        PdfGenerator report = reportBuilder.createGenerator();
                        report.render(catalogueMetrics, translation);

                        message.reply(report.toBuffer());
                    });
                })
                .onFailure(cause -> message.fail(-1, cause.getMessage()));
    }

    private Future<JsonObject> getCatalogueMetrics(String catalogueId) {
        return Future.future(promise ->
                client.getAbs(metricsAddress + "/catalogues/" + catalogueId)
                        .expect(ResponsePredicate.SC_OK)
                        .send()
                        .onSuccess(response -> {
                            JsonObject result = response.bodyAsJsonObject();
                            if (result.getBoolean("success", false)) {
                                JsonArray results = result.getJsonObject("result", new JsonObject())
                                        .getJsonArray("results", new JsonArray());
                                promise.complete((JsonObject) results.stream().findFirst().orElse(new JsonObject()));
                            } else {
                                promise.fail("Metrics not found");
                            }
                        })
                        .onFailure(promise::fail)
        );
    }

}
