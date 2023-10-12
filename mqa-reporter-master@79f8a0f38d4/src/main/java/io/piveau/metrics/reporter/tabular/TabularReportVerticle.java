package io.piveau.metrics.reporter.tabular;

import io.piveau.metrics.reporter.model.Catalogue;
import io.piveau.metrics.reporter.model.ReportTask;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.*;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.json.JsonObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TabularReportVerticle extends AbstractVerticle {

    public static final String ADDRESS = "tabular-report-generator";

    private static final Logger log = LoggerFactory.getLogger(TabularReportVerticle.class);

    private JsonObject translations;
    private JsonObject countries;

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().<String>consumer(ADDRESS, message -> {
            // regular JSON decode() / mapTo() doesn't work
            JsonObject received = new JsonObject(message.body());
//            ReportTask task = received.mapTo(ReportTask.class);
            ReportTask task = new ReportTask(
                    new Locale(received.getString("languageCode")),
                    received.getJsonArray("metrics").stream()
                            .map(entry -> (JsonObject) entry)
                            .collect(Collectors.toList())
            );

            String reportFileXlsx = task.getPath() + "global.xlsx";
            String reportTmpFileXlsx = reportFileXlsx + ".tmp";

            /* Reports are created as follows:
             * - Global XLSX report
             * - Copy global report #catalogue number of times
             * - Remove all sheets != catalogue
             * - Convert all existing XLSX reports into ODS
             */

            createXlsx(task, reportFileXlsx, reportTmpFileXlsx).compose(globalXlsx -> {
                log.info("Generating XLSX reports for language [{}]", task.getLanguageCode());
                return generateCatalogueReports(reportFileXlsx, task);
            }).onSuccess(createXlsx -> {
                log.info("Generated XLSX reports for language [{}]", task.getLanguageCode());

                vertx.fileSystem().readDir(task.getPath(), readReports -> {
                    if (readReports.succeeded()) {
                        log.debug("Generating ODS reports for language [{}]", task.getLanguageCode());
                        CompositeFuture.all(readReports.result().stream()
                                        .filter(fileName -> fileName.endsWith(".xlsx"))
                                        .map(catalogueReport ->
                                                Future.<Void>future(createCatalogueOds -> {
                                                    String reportFileOds = catalogueReport.substring(0, catalogueReport.lastIndexOf(".")) + ".ods";
                                                    createOds(catalogueReport, reportFileOds, createCatalogueOds);
                                                }))
                                        .collect(Collectors.toList()))
                                .onSuccess(success -> log.info("Generated ODS reports for language [{}] done.", task.getLanguageCode()))
                                .onFailure(failure -> log.error("Failed to generate ODS reports for language [{}]", task.getLanguageCode(), failure.getCause()));
                    } else {
                        log.error("Failed to read XLSX reports for language [{}]", task.getLanguageCode(), readReports.cause());
                    }
                });
            }).onFailure(failure -> {
                log.error("Failed to generate XLSX report, cleaning up...", failure);

                vertx.fileSystem().exists(reportTmpFileXlsx, fileExists -> {
                    if (fileExists.succeeded() && fileExists.result()) {
                        vertx.fileSystem().delete(reportTmpFileXlsx, deleteFile -> {
                            if (deleteFile.failed())
                                log.error("Failed to remove XLSX tmp file", deleteFile.cause());
                        });
                    }
                });
            });
        });

        Promise<Void> readTranslations = Promise.promise();
        vertx.fileSystem().readFile("i18n/lang.json", readFile -> {
            if (readFile.succeeded()) {
                translations = new JsonObject(readFile.result());
                readTranslations.complete();
            } else {
                readTranslations.fail(readFile.cause());
            }
        });

        Promise<Void> readCountries = Promise.promise();
        vertx.fileSystem().readFile("i18n/countries.json", readFile -> {
            if (readFile.succeeded()) {
                countries = new JsonObject(readFile.result());
                readCountries.complete();
            } else {
                readCountries.fail(readFile.cause());
            }
        });

        CompositeFuture.all(readTranslations.future(), readCountries.future())
                .onSuccess(success -> startPromise.complete())
                .onFailure(startPromise::fail);
    }

    private Future<Void> createXlsx(ReportTask task, String reportFile, String reportTmpFile) {
        return Future.future(createXlsx ->
                vertx.fileSystem().createFile(reportTmpFile, createFile -> {
                    if (createFile.succeeded()) {
                        Translator translator = new Translator(task.getLanguageCode(), translations, countries);

                        List<Catalogue> catalogues = new ArrayList<>();
                        for (JsonObject c : task.getMetrics()) {
                            Catalogue catalogue = new Catalogue(c);
                            catalogue.generateMetricSections(translator);
                            catalogues.add(catalogue);
                        }

                        vertx.executeBlocking(createXlsReport -> {
                            try {
                                XlsReport xlsReport = new XlsReport(catalogues, task.getMetrics(), translator, reportTmpFile);
                                xlsReport.createXlsReport();
                                createXlsReport.complete();
                            } catch (IOException e) {
                                createXlsReport.fail(e);
                            }
                        }, result -> {
                            if (result.succeeded()) {
                                renameTmpFile(reportTmpFile, reportFile, createXlsx);
                            } else {
                                createXlsx.fail(result.cause());
                            }
                        });
                    } else {
                        createXlsx.fail(createFile.cause());
                    }
                }));
    }

    private Future<Void> generateCatalogueReports(String globalFileName, ReportTask task) {
        return Future.future(generateCatalogues -> {
            try (FileInputStream globalXSSF = new FileInputStream(globalFileName)) {
                XSSFWorkbook globalReport = new XSSFWorkbook(globalXSSF);

                CompositeFuture.all(IntStream.range(1, globalReport.getNumberOfSheets())
                                // map catalogue title to catalogue ID
                                .mapToObj(catalogueSheetNumber -> Future.<Void>future(generateCatalogueReport -> task.getMetrics().stream()
                                        .filter(c -> c.getJsonObject("info").getString("title").toLowerCase()
                                                .contains(globalReport.getSheetName(catalogueSheetNumber).toLowerCase()))
                                        .findFirst()
                                        .ifPresentOrElse(c -> {
                                            String catalogueFileName = globalFileName.substring(0, globalFileName.lastIndexOf("/") + 1)
                                                    + c.getJsonObject("info").getString("id")
                                                    + ".xlsx.tmp";

                                            try (OutputStream catalogueXSSF = new FileOutputStream(catalogueFileName)) {
                                                XSSFWorkbook catalogueReport = cloneWorkbook(globalReport);

                                                for (int i = globalReport.getNumberOfSheets() - 1; i >= 0; i--) {
                                                    if (i != catalogueSheetNumber) {
                                                        catalogueReport.removeSheetAt(i);
                                                    }
                                                }

                                                catalogueReport.write(catalogueXSSF);
                                                catalogueReport.close();

                                                renameTmpFile(catalogueFileName, catalogueFileName.substring(0, catalogueFileName.lastIndexOf(".tmp")), generateCatalogueReport);
                                            } catch (IOException e) {
                                                generateCatalogueReport.fail(e);
                                                System.out.println("generateCatalogueReports:IOException:" + e.getMessage());
                                            }
                                        }, () -> log.error("Could not find catalogue ID for title [{}]", globalReport.getSheetName(catalogueSheetNumber)))))
                                .collect(Collectors.toList()))
                        .onSuccess(success -> generateCatalogues.complete())
                        .onFailure(failure -> generateCatalogues.fail(failure.getCause()));
            } catch (IOException e) {
                generateCatalogues.fail(e);
            }
        });
    }

    private void createOds(String xlsxReport, String reportFile, Handler<AsyncResult<Void>> handler) {
        String reportTmpFile = reportFile + ".tmp";
        vertx.executeBlocking(convertXlsxToOds -> {
            try (FileInputStream xlsxInput = new FileInputStream(xlsxReport)) {

                XSSFWorkbook xlsReport = new XSSFWorkbook(xlsxInput);

                // get row and column counts
                int maxRowCount = 0;
                int maxColumnCount = 0;
                for (org.apache.poi.ss.usermodel.Sheet sheet : xlsReport) {
                    maxRowCount = Math.max(sheet.getLastRowNum() + 1, maxRowCount);

                    for (Row row : sheet)
                        maxColumnCount = row.getLastCellNum() > maxColumnCount ? row.getLastCellNum() : maxColumnCount;
                }

                // create ods workbook
                SpreadSheet odsReport = SpreadSheet.create(xlsReport.getNumberOfSheets(), maxColumnCount, maxRowCount);

                // copy each sheet to ods file
                for (int sheetNumber = 0; sheetNumber < xlsReport.getNumberOfSheets(); sheetNumber++) {
                    org.apache.poi.ss.usermodel.Sheet xlsSheet = xlsReport.getSheetAt(sheetNumber);
                    Sheet odsSheet = odsReport.getSheet(sheetNumber);
                    odsSheet.setName(xlsSheet.getSheetName());

                    for (Row row : xlsSheet) {
                        for (int columnNumber = row.getFirstCellNum(); columnNumber < row.getLastCellNum(); columnNumber++) {
                            // check if call has String or Numeric value and copy cell value to according xls cell
                            if (row.getCell(columnNumber).getCellType() == CellType.NUMERIC) {
                                odsSheet.setValueAt(row.getCell(columnNumber).getNumericCellValue(), columnNumber, row.getRowNum());
                            } else {
                                odsSheet.setValueAt(row.getCell(columnNumber).getStringCellValue(), columnNumber, row.getRowNum());
                            }
                        }
                    }
                }

                writeOdsFile(odsReport, reportTmpFile, reportFile, handler);

            } catch (IOException e) {
                handler.handle(Future.failedFuture(e));
            }
        }, handler);
    }

    private void writeOdsFile(SpreadSheet spreadSheet, String tmpFile, String reportFile, Handler<AsyncResult<Void>> handler) {
        try (FileOutputStream odsOutput = new FileOutputStream(tmpFile, false); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            log.debug("Try to writeOdsFile [{}] ...", reportFile);
            spreadSheet.getPackage().save(out);
            byte[] odsReportAsByteArray = out.toByteArray();
            odsOutput.write(odsReportAsByteArray);
            renameTmpFile(tmpFile, reportFile, handler);
        } catch (IOException e) {
            handler.handle(Future.failedFuture(e));
        }
    }

    private void renameTmpFile(String replacement, String fileToReplace, Handler<AsyncResult<Void>> handler) {
        vertx.fileSystem().move(replacement, fileToReplace, new CopyOptions().setReplaceExisting(true), replaceFile -> {
            if (replaceFile.succeeded()) {
                handler.handle(Future.succeededFuture());
            } else {
                log.error("Failed to replace [{}] with [{}], cleaning up...", fileToReplace, replacement, replaceFile.cause());
                vertx.fileSystem().delete(replacement).onFailure(failure -> log.error("Failed to delete file [{}]", replacement, failure.getCause()));
                handler.handle(Future.failedFuture(replaceFile.cause()));
            }

                /*
                vertx.fileSystem().delete(replacement, ar -> {
                    if (ar.failed()) {
                        log.error("Failed to delete file [{}]", replacement, ar.cause());
                    }

                    handler.handle(Future.failedFuture(replaceFile.cause()));
                });

                 */
        });
    }


    /*private XSSFWorkbook cloneWorkbook(XSSFWorkbook workbook) throws IOException, InvalidFormatException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return XSSFWorkbookFactory.createWorkbook(OPCPackage.create(outputStream));
        }
    }*/

    private XSSFWorkbook cloneWorkbook(XSSFWorkbook workbook) throws IOException {
        Path temp = Files.createTempFile("workbook", ".xlsx");
        FileInputStream fis = new FileInputStream(temp.toFile());
        FileOutputStream outfile = new FileOutputStream(temp.toFile());
        try {
            workbook.write(outfile);
            XSSFWorkbook workbookCloned = XSSFWorkbookFactory.createWorkbook(OPCPackage.open(fis));
            return workbookCloned;
        } catch (Exception e) {
            log.error("cloneWorkbook:Exception=", e.getMessage());
            return null;
        } finally {
            outfile.close();
            fis.close();
        }
    }




}

