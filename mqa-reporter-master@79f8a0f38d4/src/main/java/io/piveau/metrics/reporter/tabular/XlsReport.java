package io.piveau.metrics.reporter.tabular;

import io.piveau.metrics.reporter.model.Catalogue;
import io.piveau.metrics.reporter.model.MetricSection;
import io.piveau.metrics.reporter.model.Translation;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class XlsReport {

    private final List<Catalogue> catalogues;
    private final List<JsonObject> metrics;
    private final Translator translator;
    private final String fileName;

    public XlsReport(List<Catalogue> catalogues, List<JsonObject> metrics, Translator translator, String fileName) {
        this.catalogues = catalogues;
        this.metrics = metrics;
        this.translator = translator;
        this.fileName = fileName;
    }

    private String truncatecatalogueTitle(String text ) {
        if (text.length() <= 30) {
            return text;
        } else {
            return text.substring(0, 30);
        }
    }

    public void createXlsReport() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); OutputStream outputStream = new FileOutputStream(fileName)) {

            Translation translations = translator.getTranslations();

            // creating a font of height 12pt and bold font weight with blue colored background
            XSSFCellStyle styleBoldFontBlueBackground = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            font.setItalic(false);
            styleBoldFontBlueBackground.setFont(font);
            styleBoldFontBlueBackground.setFillForegroundColor(new XSSFColor(new Color(159, 242, 223), new DefaultIndexedColorMap()));
            styleBoldFontBlueBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // creating a bold font
            XSSFCellStyle styleBoldFont = workbook.createCellStyle();
            styleBoldFont.setFont(font);

            // inserting overview page
            XSSFSheet overviewSheet = workbook.createSheet(translations.getNavigationDashboard());
            XSSFRow overviewMainHeadingRow = overviewSheet.createRow(0);
            overviewMainHeadingRow.createCell(0).setCellValue(translations.getNavigationDashboard());
            overviewMainHeadingRow.getCell(0).setCellStyle(styleBoldFont);

            String[] headerCells = {translations.getCatalogueCountry(),
                    translations.getCatalogueName(),
                    translations.getAccessibilityAccessUrl(),
                    translations.getAccessibilityDownloadUrl(),
                    translations.getDcatApCompliance(),
                    translations.getMachineReadability(),
                    translations.getCatalogueRating()
            };

            int overviewRowCount = 2;

            // inserting header cells for overview page
            XSSFRow overviewHeaderCellsRow = overviewSheet.createRow(overviewRowCount++);
            overviewHeaderCellsRow.setRowStyle(styleBoldFontBlueBackground);
            int columnIndex = 0;
            for (String cellName : headerCells) {
                overviewHeaderCellsRow.createCell(columnIndex).setCellValue(cellName);
                overviewHeaderCellsRow.getCell(columnIndex).setCellStyle(styleBoldFontBlueBackground);
                columnIndex++;
            }

            // iterating over data for overview table and adding it to the sheet
            for (JsonObject catalogueMetric : metrics) {
                int cellCount = 0;
                JsonObject info = catalogueMetric.getJsonObject("info");

                XSSFRow row = overviewSheet.createRow(overviewRowCount++);
                JsonObject countries = translator.getSectionTranslations().getJsonObject("countries");

                // catalogue country
                if (info.getString("spatial") == null || !countries.containsKey(info.getString("spatial"))) {
                    row.createCell(cellCount++).setCellValue("-");
                } else {
                    row.createCell(cellCount++).setCellValue(countries.getString(info.getString("spatial")));
                }

                // catalogue title
                row.createCell(cellCount++).setCellValue(getStringValue(info, "title"));

                JsonObject accessibility = catalogueMetric.getJsonObject("accessibility");
                // accessibility access URL
                row.createCell(cellCount++).setCellValue(getArrayStringValue(accessibility, "accessUrlStatusCodes"));
                // accessibility download URL
                row.createCell(cellCount++).setCellValue(getArrayStringValue(accessibility, "downloadUrlStatusCodes"));

                JsonObject interoperability = catalogueMetric.getJsonObject("interoperability");
                // DCAT-AP compliance
                row.createCell(cellCount++).setCellValue(getArrayStringValue(interoperability, "dcatApCompliance"));
                // machine readability
                row.createCell(cellCount++).setCellValue(getArrayStringValue(interoperability, "formatMediaTypeMachineReadable"));

                row.createCell(cellCount).setCellValue(getScoreValue(catalogueMetric));
            }

            // inserting sheets for catalogues
            for (Catalogue catalogue : catalogues) {
                // Creating Sheet with Catalogue title as name
                //XSSFSheet spreadSheet = workbook.createSheet(catalogue.getTitle() + " - " + catalogue.getId());
                //String sheetTitleId = catalogue.getTitle() + " - " + catalogue.getId();

                String sheetTitle = truncatecatalogueTitle(catalogue.getTitle());

                //XSSFSheet spreadSheet = workbook.createSheet(catalogue.getTitle());

                XSSFSheet spreadSheet;
                int index = workbook.getSheetIndex(sheetTitle);
                if (index != -1) {
                    spreadSheet = workbook.getSheetAt(index);
                } else {
                    spreadSheet = workbook.createSheet(sheetTitle);
                }

                // setting catalogue title as title in the first row of the sheet
                XSSFRow catalogueHeaderRow = spreadSheet.createRow(0);
                catalogueHeaderRow.createCell(0).setCellValue(catalogue.getTitle());
                catalogueHeaderRow.getCell(0).setCellStyle(styleBoldFont);

                int rowCount = 2;
                for (MetricSection section : catalogue.getSections()) {
                    // add some empty rows
                    spreadSheet.createRow(rowCount++);
                    spreadSheet.createRow(rowCount++);
                    // setting section heading
                    XSSFRow sectionHeaderRow = spreadSheet.createRow(rowCount++);
                    sectionHeaderRow.setRowStyle(styleBoldFontBlueBackground);
                    sectionHeaderRow.createCell(0).setCellValue(section.getSectionHeading());
                    sectionHeaderRow.getCell(0).setCellStyle(styleBoldFontBlueBackground);

                    for (Map.Entry<String, Object> sectionEntry : section.getMetrics()) {
                        // FIXME
                        // dirty fix to prevent class cast errors when encountering dimension score
                        if (sectionEntry.getValue() instanceof JsonArray) {

                            // setting heading of the indicator
                            XSSFRow indicatorHeaderRow = spreadSheet.createRow(rowCount++);
                            indicatorHeaderRow.createCell(0).setCellValue(translator.getSectionTranslations().getString(sectionEntry.getKey()));
                            indicatorHeaderRow.getCell(0).setCellStyle(styleBoldFont);

                            JsonArray indicatorData = (JsonArray) sectionEntry.getValue();

                            if (!indicatorData.isEmpty()) {

                                ArrayList<String> keys = new ArrayList<>();
                                ArrayList<Double> values = new ArrayList<>();
                                for (int i = 0; i < indicatorData.size(); i++) {
                                    JsonObject indicator = indicatorData.getJsonObject(i);
                                    if (indicator.getString("name").equals("yes")) {
                                        keys.add(translations.getMetricLabelYes());
                                    } else if (indicator.getString("name").equals("no")) {
                                        keys.add(translations.getMetricLabelNo());
                                    } else {
                                        keys.add(indicator.getString("name"));
                                    }
                                    values.add(indicator.getDouble("percentage"));
                                }
                                // save keys and values to two ArrayLists, print keys in one row and values in a row below it
                                XSSFRow indicatorKeysRow = spreadSheet.createRow(rowCount++);
                                int keyCells = 0;
                                for (String key : keys) {
                                    indicatorKeysRow.createCell(keyCells++).setCellValue(key);
                                }
                                XSSFRow indicatorValuesRow = spreadSheet.createRow(rowCount++);
                                int valueCells = 0;
                                for (Double value : values) {
                                    indicatorValuesRow.createCell(valueCells++).setCellValue(value);
                                }
                            } else {
                                XSSFRow indicatorNotFoundRow = spreadSheet.createRow(rowCount++);
                                indicatorNotFoundRow.createCell(0).setCellValue("n/a");
                            }
                        }
                        spreadSheet.createRow(rowCount++);
                    }
                }
            }

            workbook.write(outputStream);
        }
    }

    private String getStringValue(JsonObject motherObject, String key) {
        if (motherObject.getString(key) == null) {
            return "n/a";
        } else {
            return motherObject.getString(key);
        }
    }

    private String getArrayStringValue(JsonObject motherObject, String key) {
        JsonArray keyArray = motherObject.getJsonArray(key);
        if (keyArray == null || keyArray.isEmpty()) {
            return "n/a";
        } else {
            String returnString = "-";
            for (Object item : keyArray) {
                JsonObject itemObject = (JsonObject) item;
                if (itemObject.getString("name").equals("yes") || itemObject.getString("name").equals("200")) {
                    returnString = itemObject.getDouble("percentage").toString();
                }
            }
            return returnString;
        }
    }

    private String getScoreValue(JsonObject motherObject) {
        if (motherObject.getInteger("score") == null) {
            return "-";
        } else {
//            Excellent: 351 - 405
//            Good: 221 - 350
//            Sufficient: 121 - 220
//            Bad: 0 - 120
            int score = motherObject.getInteger("score");
            if (score < 121) {
                return translator.getTranslations().getScoreBad();
            } else if (score < 221) {
                return translator.getTranslations().getScoreSufficient();
            } else if (score < 351) {
                return translator.getTranslations().getScoreGood();
            } else if (score < 405) {
                return translator.getTranslations().getScoreExcellent();
            } else {
                return "n/a";
            }
        }
    }
}
