package io.piveau.metrics.reporter.pdf;

import io.piveau.metrics.reporter.model.Catalogue;
import io.piveau.metrics.reporter.model.MetricSection;
import io.piveau.metrics.reporter.model.ReportTask;
import io.piveau.metrics.reporter.model.Translation;
import io.piveau.metrics.reporter.model.chart.Chart;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import rst.pdfbox.layout.elements.*;
import rst.pdfbox.layout.text.*;
import rst.pdfbox.layout.text.annotations.AnnotatedStyledText;
import rst.pdfbox.layout.text.annotations.Annotations;
import org.vandeseer.easytable.structure.cell.paragraph.Markup;


import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static io.piveau.metrics.reporter.ApplicationConfig.*;



public class PdfReport {
    private static final Logger log = LoggerFactory.getLogger(PdfReport.class);

    private static final float LINE_SPACING = 1.45f;
    private static final Color lineColor = new Color(114, 214, 234);
    private static final float MARGIN_LEFT = 36;
    private static final float MARGIN_RIGHT = 40;
    private static final float MARGIN_TOP = 41;
    private static final float MARGIN_BOTTOM = 50;
    public static final float TableStartY = 150f;

    private final float pageWidth;
    private final float pageHeight;

    private final Styles  styles;
    private final Translation translation;
    private final Translator translator;
    private final ReportTask task;
    private final Vertx vertx;
    private final JsonObject config;

    // PDF Layout Document
    private final Document document;
    // PDFBox Document
    private PDDocument pdDocument;

//    private Pattern urlPattern = Pattern.compile(
//            "((www|http:|https:)//){0,1}(([a-zA-Z])+)(\\.(([a-zA-Z])+))+/{0,1}((([a-zA-Z])+)(\\.(([a-zA-Z])+))*/{0,1})*",
//            Pattern.CASE_INSENSITIVE);

    private Pattern htmlPattern = Pattern.compile("</?[a-zA-Z]+>");

    public PdfReport(ReportTask task, Styles styles, Translator translator, Vertx vertx, JsonObject config) throws IOException {
        this.vertx = vertx;
        this.config = config;
        this.task = task;
        this.translator = translator;
        this.styles = styles;

        this.translation = translator.getTranslations();

//        JsonObject scoreTranslations = new JsonObject()
//                .put("good", translation.getScoreGood())
//                .put("excellent", translation.getScoreExcellent())
//                .put("bad", translation.getScoreBad())
//                .put("sufficient", translation.getScoreSufficient());
//        this.translator.getCountries().put("scoreTranslations", scoreTranslations);

        // create PDF Layout Document
        document = new Document(
                MARGIN_LEFT,
                MARGIN_RIGHT,
                MARGIN_TOP,
                MARGIN_BOTTOM);

        pageWidth = document.getPageFormat().getMediaBox().getWidth();
        pageHeight = document.getPageFormat().getMediaBox().getHeight();

        Buffer buffer = vertx.fileSystem().readFileBlocking(config.getString(ENV_HEADER_IMAGE, DEFAULT_HEADER_IMAGE));
        ImageElement headerImage = new ImageElement(new ByteArrayInputStream(buffer.getBytes()));
        headerImage.setWidth(ImageElement.SCALE_TO_RESPECT_WIDTH);
        headerImage.setHeight(ImageElement.SCALE_TO_RESPECT_WIDTH);

        document.add(headerImage);

        // initialize Styles object with the current PDFBox document
        this.styles.initializeFonts(document.getPDDocument());
    }

    public void addTitlePage() {
        try {
            Paragraph titleParagraph = new Paragraph();
            titleParagraph.setAlignment(Alignment.Center);
            titleParagraph.add(new NewLine());

            titleParagraph.addMarkup(convertURLsToMarkup(translation.getPortalUrl().trim()),
                    styles.getHeadingTwoStyle().getFontSize(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont());
            titleParagraph.add(new NewLine());
            titleParagraph.add(new NewLine());

            titleParagraph.addText(translation.getMqaTitle().trim(),
                    styles.getHeadingOneStyle().getFontSize(),
                    styles.getHeadingOneStyle().getFont());
            titleParagraph.add(new NewLine());
            titleParagraph.add(new NewLine());

            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            titleParagraph.addText(date,
                    styles.getParagraphStyle().getFontSize(),
                    styles.getParagraphStyle().getFont());

            document.add(titleParagraph);
        } catch (IOException e) {
            log.error("Add title", e);
        }
    }

    public Future<Void> addMethodologyPage() {
        return vertx.executeBlocking(promise -> {
            try {
                document.add(ControlElement.NEWPAGE);

                Paragraph heading = new Paragraph();
                heading.addText(translation.getMethodologyHeadline().trim(),
                        styles.getHeadingOneStyle().getFontSize(),
                        styles.getHeadingOneStyle().getFont());
                heading.setAlignment(Alignment.Left);
                document.add(heading);
                document.add(new VerticalSpacer(40));
                // TODO: Add TOC Entry

                Paragraph methodologySubHeadlineParagraph = new Paragraph();
                methodologySubHeadlineParagraph.addText(translation.getMethodologySubHeadline().trim(),
                        styles.getHeadingTwoStyle().getFontSize(),
                        styles.getHeadingTwoStyle().getFont());
                document.add(methodologySubHeadlineParagraph);
                document.add(new VerticalSpacer(5));
                // TODO: Add TOC Entry

                // methodology Intro
                Paragraph methodologyIntroTextParagraph = createParagraphFromHTML(translation.getMqaIntro().trim());
                methodologyIntroTextParagraph.setLineSpacing(LINE_SPACING);
                document.add(methodologyIntroTextParagraph);
                document.add(new VerticalSpacer(19));


                String mqaLink = "<a href=\""
                        + config.getString(ENV_METHODOLOGY_URL, DEFAULT_METHODOLOGY_URL)
                        + "\" target=\"_blank\">"
                        + translation.getMethodologyHeadline()
                        + "</a>";
                Paragraph methodologyLink = createParagraphFromHTML(mqaLink);
                document.add(methodologyLink);
                document.add(new VerticalSpacer(25));

                // methodology scope
                Paragraph methodologyScopeHeadline = new Paragraph();
                methodologyScopeHeadline.addText(translation.getMethodologyScopeHeadline().trim(),
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                document.add(methodologyScopeHeadline);
                // TODO: Add TOC Entry


                Paragraph methodologyScopeMainText = createParagraphFromHTML(translation.getMethodologyScopeMainText().trim());
                methodologyScopeMainText.setLineSpacing(LINE_SPACING);
                document.add(methodologyScopeMainText);
                document.add(new VerticalSpacer(25));


                //What do we not cover
                Paragraph methodologyCoverHeadline = new Paragraph();
                methodologyCoverHeadline.addText(translation.getMethodologyCoverHeadline().trim(),
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                document.add(methodologyCoverHeadline);
                // TODO: Add TOC Entry

                Paragraph methodologyCoverMainText = createParagraphFromHTML(translation.getMethodologyCoverMainText().trim());
                methodologyCoverMainText.setLineSpacing(LINE_SPACING);
                methodologyCoverMainText.add(
                        new NewLine(
                                new FontDescriptor(
                                        styles.getParagraphStyle().getFont(),
                                        styles.getParagraphStyle().getFontSize())));
                document.add(methodologyCoverMainText);

                // DCAT-AP SHACL validation service web page
                String shaclLink = "<a href=\""
                        + config.getString(ENV_SHACL_URL, DEFAULT_SHACL_URL)
                        + "\" target=\"_blank\">"
                        + translation.getMethodologyCoverLinkText()
                        + "</a>";
                Paragraph methodologyCoverLink = createParagraphFromHTML(shaclLink);
                document.add(methodologyCoverLink);
                document.add(new VerticalSpacer(15));

                //DCAT-AP SHACL validation service API
                String shacl_API_Link = "<a href=\""
                        + config.getString(ENV_SHACL_API_URL, DEFAULT_SHACL_API_URL)
                        + "\" target=\"_blank\">"
                        + translation.getMethodologyCoverApiLinkText()
                        + "</a>";
                Paragraph methodologyCoverApiLink = createParagraphFromHTML(shacl_API_Link);
                document.add(methodologyCoverApiLink);
                document.add(new VerticalSpacer(25));

                //The MQA Process
                Paragraph methodologyProcessHeadline = new Paragraph();
                methodologyProcessHeadline.addText(translation.getMethodologyProcessHeadline().trim(),
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                document.add(methodologyProcessHeadline);
                // TODO: Add TOC Entry

                Paragraph methodologyProcessMainText = createParagraphFromHTML(translation.getMethodologyProcessMainText().trim());
                methodologyProcessMainText.setLineSpacing(LINE_SPACING);
                document.add(methodologyProcessMainText);
                document.add(new VerticalSpacer(25));

                //Assumptions
                Paragraph methodologyAssumptionsHeadline = new Paragraph();
                methodologyAssumptionsHeadline.addText(translation.getMethodologyAssumptionsHeadline().trim(),
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                document.add(methodologyAssumptionsHeadline);

                Paragraph methodologyAssumptionsMainText = createParagraphFromHTML(translation.getMethodologyAssumptionsMainText().trim());
                methodologyAssumptionsMainText.setLineSpacing(LINE_SPACING);
                document.add(methodologyAssumptionsMainText);
                document.add(new VerticalSpacer(25));

                //Dimensions
                Paragraph methodologyDimensionsHeadline = new Paragraph();
                methodologyDimensionsHeadline.addText(translation.getMethodologyDimensionsHeadline().trim(),
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                document.add(methodologyDimensionsHeadline);

                Paragraph methodologyDimensionsMainText = createParagraphFromHTML(translation.getMethodologyDimensionsMainText().trim());
                methodologyDimensionsMainText.setLineSpacing(LINE_SPACING);
                document.add(methodologyDimensionsMainText);
                document.add(new VerticalSpacer(25));


                addFindabilityDashboard();

                addAccessibilityDashboard();

                addInteroperabilityDashboard();

                addReusabilityDashboard();

                addContextualityDashboard();

                addRatingDashboard();

                changeDocumentState();
                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    private void changeDocumentState() throws IOException {
        pdDocument = document.render();

        PDPageContentStream contentStream = new PDPageContentStream(
                pdDocument,
                // TODO: if TOC is used, the page index has to be calculated dynamically
                pdDocument.getPage(1),
                PDPageContentStream.AppendMode.APPEND,
                false,
                true);
        setBlueStroke(contentStream, 70f);
        contentStream.close();
    }

    public Future<Void> addCatalogueDataToDocument(Catalogue catalogue) {
        // This execution block is not ordered (ordered=false), so that it can be executed in parallel (for single reports).
        // If sequential execution is required, it has to be synchronized at Future Level (for global report).
        return vertx.executeBlocking(promise -> {
            try {
                for (MetricSection section : catalogue.getSections()) {
                    // New Page
                    PDPage page = new PDPage(PDRectangle.A4);
                    pdDocument.addPage(page);
                    PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);

                    //Heading
                    TextFlow catalogueTitle = TextFlowUtil.createTextFlow(catalogue.getTitle(),
                            styles.getHeadingThreeStyle().getFontSize(),
                            styles.getHeadingThreeStyle().getFont());
                    catalogueTitle.drawText(contentStream, new Position(MARGIN_LEFT, pageHeight - 42f), Alignment.Left, null);

                    TextFlow sectionHeading = TextFlowUtil.createTextFlow(section.getSectionHeading(),
                            styles.getHeadingTwoStyle().getFontSize(),
                            styles.getHeadingTwoStyle().getFont());
                    sectionHeading.drawText(contentStream, new Position(MARGIN_LEFT, pageHeight - 72f), Alignment.Left, null);
                    //TODO: Add TOC Entry: catalogue.getTitle() + " - " + section.getSectionHeading()

                    // Line
                    setBlueStroke(contentStream, 100f);
                    contentStream.close();

                    CatalogueTableBuilder catalogueTableBuilder =
                            new CatalogueTableBuilder(pdDocument, styles, (pageWidth - MARGIN_RIGHT - MARGIN_LEFT) / 2);

                    for (Chart chart : section.getCharts()) {
                        if (chart.getName() != null) {
                            catalogueTableBuilder.addChart(chart);
                        }
                    }

                    // Draw table
                    Table chartTable = catalogueTableBuilder.getChartTable();
                    TableDrawer chartTableDrawer = TableDrawer.builder()
                            .startX(MARGIN_LEFT)
                            .startY(pageHeight - 105f)
                            .endY(MARGIN_BOTTOM)
                            .table(chartTable)
                            .build();

                    if (chartTable != null && !chartTable.getRows().isEmpty()) {
                        // Table can be drawn over multiple pages
                        chartTableDrawer.draw(
                                () -> pdDocument,
                                () -> new PDPage(PDRectangle.A4),
                                MARGIN_TOP);
                    }
                }
                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    public Future<Void> addAllCatalogueDataToDocument(List<Catalogue> completedCatalogues) {
        Future<Void> entryFuture = Future.future(Promise::complete);
        for (Catalogue catalogue : completedCatalogues) {
            entryFuture = entryFuture.compose(v -> addCatalogueDataToDocument(catalogue));
        }

        return entryFuture;
    }

    private void setBlueStroke(PDPageContentStream contentStream, float yOffset) throws IOException {
        contentStream.setLineWidth(1.5f);
        contentStream.setStrokingColor(lineColor);
        contentStream.moveTo(MARGIN_LEFT, pageHeight - yOffset);
        contentStream.lineTo(pageWidth - MARGIN_RIGHT, pageHeight - yOffset);
        contentStream.stroke();
    }


    public Future<Void> addFindabilityDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Findability
                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyFindabilityHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow findabilityMainText = TextFlowUtil.createTextFlow(translation.getMethodologyFindabilityMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                findabilityMainText.setMaxWidth(pageWidth - 40);


                findabilityMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder findabilityTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth + 30, tableRowWidth * 2, tableRowWidth * 3, tableRowWidth - 30)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetricName()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableDesc()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetric()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableWeight()).build())
                                        .build());

                // Build rows

                // Keyword usage
                findabilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyTableKeywordusage()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_desc_1()).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_metric_1()+ "\n\nDataset\n" +
                                        "dcat:keyword\n").build())
                                .add(TextCell.builder().text("30").build())
                                .build());

                // Categories
                findabilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyTableCategories()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_desc_2()).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_metric_2()+ "\n\nDataset\n" +
                                        "dcat:theme\n").build())
                                .add(TextCell.builder().text("30").build())
                                .build());

                // Geosearch
                findabilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyTableGeosearch()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_desc_3()).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_metric_3()+ "\n\nDataset\n" +
                                        "dcat:spatial\n").build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                // Time based search
                findabilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyTableTimebasedsearch()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityTableKeywords_desc_4()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                                .append(Markup.builder().markup(translation.getMethodologyFindabilityTableKeywords_metric_4()).font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                Table findabilityTable = findabilityTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - 150f)
                        .endY(MARGIN_BOTTOM)
                        .table(findabilityTable)
                        .build();

                if (findabilityTable != null && !findabilityTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }


    public Future<Void> addAccessibilityDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Accessibility

                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyAccessibilityHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow accessibilityMainText = TextFlowUtil.createTextFlow(translation.getMethodologyAccessibilityMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                accessibilityMainText.setMaxWidth(pageWidth - 40);


                accessibilityMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder accessibilityTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth + 30 , tableRowWidth * 2, tableRowWidth * 3, tableRowWidth - 30)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetricName()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableDesc()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetric()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableWeight()).build())
                                        .build());

                // Build rows Accessibility

                // AccessURL accessibility
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_name_1()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_desc_1()).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_metric_1()+ "\n\nDistribution\n" +
                                        "dcat:accessURL\n").build())
                                .add(TextCell.builder().text("50").build())
                                .build());

                // DownloadURL
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_name_2()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_desc_2()).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_metric_2()+ "\n\nDistribution\n" +
                                        "dcat:downloadURL\n").build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                // DownloadURL accessibility
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_name_3()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_desc_3()).build())
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityTableKeywords_metric_3()+ "\n\nDistribution\n" +
                                        "dcat:downloadURL\n").build())
                                .add(TextCell.builder().text("30").build())
                                .build());



                Table dashboardTable = accessibilityTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - 150f)
                        .endY(MARGIN_BOTTOM)
                        .table(dashboardTable)
                        .build();

                if (dashboardTable != null && !dashboardTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }


    public Future<Void> addInteroperabilityDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Interoperability

                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyInteroperabilityHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow interoperabilityMainText = TextFlowUtil.createTextFlow(translation.getMethodologyInteroperabilityMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                interoperabilityMainText.setMaxWidth(pageWidth - 40);


                interoperabilityMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder accessibilityTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth + 30 , tableRowWidth * 2, tableRowWidth * 3, tableRowWidth - 30)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetricName()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableDesc()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetric()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableWeight()).build())
                                        .build());

                // Build rows Interoperability

                // Interoperability Format
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_1()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_1()).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_metric_1()+ "\n\nDistribution\n" +
                                        "dct:format\n").build())
                                .add(TextCell.builder().text("50").build())
                                .build());

                // Interoperability Media type
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_2()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_2()).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_metric_2()+ "\n\nDistribution\n" +
                                        "dcat:mediaType\n").build())
                                .add(TextCell.builder().text("10").build())
                                .build());

                // Interoperability Format / Media type from vocabulary
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_3()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_3()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                                .append(Markup.builder().markup(translation.getMethodologyInteroperabilityTableKeywords_metric_3()+ "\n\nDistribution\ndct:format\n" +
                "dcat:mediaType\n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("10").build()).build());

                // Interoperability Non-proprietary
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_4()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_4()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyInteroperabilityTableKeywords_metric_4()+ "\n\nDistribution\n" + "dct:format \n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())

                                .add(TextCell.builder().text("20").build())
                                .build());

                // Interoperability Machine readable
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_5()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_5()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyInteroperabilityTableKeywords_metric_5()+ "\n\nDistribution\n" + "dct:format \n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                //Interoperability DCAT-AP compliance
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_name_6()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityTableKeywords_desc_6()).build())
                                //.add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                //        .append(Markup.builder().markup(convertURLsToMarkup(translation.getMethodologyInteroperabilityTableKeywords_desc_6())).font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                               .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyInteroperabilityTableKeywords_metric_6()).font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("30").build())
                                .build());

                Table interoperabilityTable = accessibilityTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - TableStartY)
                        .endY(MARGIN_BOTTOM)
                        .table(interoperabilityTable)
                        .build();

                if (interoperabilityTable != null && !interoperabilityTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }


    public Future<Void> addReusabilityDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Reusability

                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyReusabilityHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow ReusabilityMainText = TextFlowUtil.createTextFlow(translation.getMethodologyReusabilityMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                ReusabilityMainText.setMaxWidth(pageWidth - 40);


                ReusabilityMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder accessibilityTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth + 30 , tableRowWidth * 2, tableRowWidth * 3, tableRowWidth - 30)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetricName()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableDesc()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetric()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableWeight()).build())
                                        .build());

                // Build rows Reusability

                // Reusability License information
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_1()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_desc_1()).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_metric_1()+ "\n\nDistribution\n" +
                                        "dct:license\n").build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                // Reusability License vocabulary
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_2()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_desc_2()).build())
                               // .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_metric_2()+ "\n\nDistribution\n" +
                                //        "dct:license\n").build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyReusabilityTableKeywords_metric_2()+ "\n\nDistribution\n" +
                                                "dct:license\n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("10").build())
                                .build());

                // Reusability Access restrictions
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_3()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_desc_3()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyReusabilityTableKeywords_metric_3()+ "\n\nDataset\ndct:accessRights\n" +
                                                "dcat:mediaType\n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("10").build()).build());

                // Reusability Access restrictions vocabulary
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_4()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_desc_4()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyReusabilityTableKeywords_metric_4()+ "\n\nDataset\n" + "dct:accessRights \n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())

                                .add(TextCell.builder().text("5").build())
                                .build());

                // Reusability Contact point
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_5()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_desc_5()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyReusabilityTableKeywords_metric_5()+ "\n\nDataset\n" + "dct:contactPoint \n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                // Reusability Publisher
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityTableKeywords_name_6()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(convertURLsToMarkup(translation.getMethodologyReusabilityTableKeywords_desc_6())).font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(ParagraphCell.builder().paragraph(org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.builder()
                                        .append(Markup.builder().markup(translation.getMethodologyReusabilityTableKeywords_metric_6()+ "\n\nDataset\n" + "dct:publisher \n").font(Markup.MarkupSupportedFont.HELVETICA).build()).build()).build())
                                .add(TextCell.builder().text("10").build())
                                .build());

                Table ReusabilityTable = accessibilityTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - TableStartY)
                        .endY(MARGIN_BOTTOM)
                        .table(ReusabilityTable)
                        .build();

                if (ReusabilityTable != null && !ReusabilityTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    public Future<Void> addContextualityDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Contextuality

                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyContextualityHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow ContextualityMainText = TextFlowUtil.createTextFlow(translation.getMethodologyContextualityMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                ContextualityMainText.setMaxWidth(pageWidth - 40);


                ContextualityMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder accessibilityTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth + 30 , tableRowWidth * 2, tableRowWidth * 3, tableRowWidth - 30)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetricName()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableDesc()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableMetric()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyTableWeight()).build())
                                        .build());

                // Build rows Contextuality

                // Contextuality Rights
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_name_1()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_desc_1()).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_metric_1()+ "\n\nDistribution\n" + "dct:rights\n").build())
                                .add(TextCell.builder().text("5").build())
                                .build());

                // Contextuality File size
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_name_2()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_desc_2()).build())
                                 .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_metric_2()+ "\n\nDistribution\n" + "dct:byteSize\n").build())

                                .add(TextCell.builder().text("5").build())
                                .build());

                // Contextuality Date of issue
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_name_3()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_desc_3()).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_metric_3()+ "\n\nDataset and Distribution\n" + "dct:issued\n").build())
                                .add(TextCell.builder().text("5").build()).build());

                // Contextuality Modification date
                accessibilityTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_name_4()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_desc_4()).build())
                                .add(TextCell.builder().text(translation.getMethodologyContextualityTableKeywords_metric_4()+ "\n\nDataset and Distribution\n" + "dct:modified\n").build())
                                .add(TextCell.builder().text("5").build())
                                .build());


                Table ContextualityTable = accessibilityTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - TableStartY)
                        .endY(MARGIN_BOTTOM)
                        .table(ContextualityTable)
                        .build();

                if (ContextualityTable != null && !ContextualityTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    //Rating tables
    public Future<Void> addRatingDashboard() {
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                //Rating
                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getMethodologyRatingHeadline().trim(),
                        styles.getHeadingFourStyle().getFontSize(),
                        styles.getHeadingFourStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);


                TextFlow RatingMainText = TextFlowUtil.createTextFlow(translation.getMethodologyRatingMainText().trim(),
                        styles.getParagraphStyle().getFontSize(),
                        styles.getParagraphStyle().getFont());
                RatingMainText.setMaxWidth(pageWidth - 40);


                RatingMainText.drawText(contentStream,  new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP - 40),
                        Alignment.Left,
                        null);


                setBlueStroke(contentStream, 70f);

                contentStream.close();

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;
                Table.TableBuilder ratingTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth * 3 , tableRowWidth * 3)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyScoringTableDimension()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyScoringTableMax_points()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                        .build());

                // Build rows Rating

                // Rating Findability
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyFindabilityHeadline()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("100").build())
                                .build());

                // Rating Accessibility
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyAccessibilityHeadline()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("100").build())
                                .build());

                // Rating Interoperability
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyInteroperabilityHeadline()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("110").build()).build());

                // Rating Reusability
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyReusabilityHeadline()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("75").build())
                                .build());

                // Rating Contextuality
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyContextualityHeadline()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("20").build())
                                .build());

                // Rating Sum
                ratingTableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getMethodologyScoringTableSum()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("405").font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .build());

                Table RatingTable = ratingTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - TableStartY)
                        .endY(pageHeight - TableStartY - 200f)
                        .table(RatingTable)
                        .build();

                if (RatingTable != null && !RatingTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }



                Table.TableBuilder ratingTablePointsBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth * 2 , tableRowWidth * 2)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getMethodologyRatingHeadline()).build())
                                        .add(TextCell.builder().text(translation.getMethodologyScoringTableRange()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                        .build());

                // Rating Excellent
                ratingTablePointsBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getScoreExcellent()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("351 - 405").build())
                                .build());

                // Rating Good
                ratingTablePointsBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getScoreGood()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("221 - 350").build()).build());

                // Rating Sufficient
                ratingTablePointsBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getScoreSufficient()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("121 - 220").build())
                                .build());

                // Rating Bad
                ratingTablePointsBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder().text(translation.getScoreBad()).font(fontBold).backgroundColor(Color.LIGHT_GRAY).build())
                                .add(TextCell.builder().text("0 - 120").build())
                                .build());



                Table RatingTablePoints = ratingTablePointsBuilder.build();
                RepeatedHeaderTableDrawer tableDrawerPoints = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - TableStartY - 150)
                        .table(RatingTablePoints)
                        .build();



                if (RatingTablePoints != null && !RatingTablePoints.getRows().isEmpty()) {
                    // Draw table Points
                    tableDrawerPoints.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            0f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }


    public Future<Void> addNavigationDashboard() {
        return vertx.executeBlocking(promise -> {
            try {
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);

                float tableRowWidth = (pageWidth - MARGIN_LEFT - MARGIN_RIGHT) / 7;

                PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
                TextFlow text = TextFlowUtil.createTextFlow(translation.getNavigationDashboard(),
                        styles.getHeadingOneStyle().getFontSize(),
                        styles.getHeadingOneStyle().getFont());
                text.drawText(contentStream, new Position(MARGIN_LEFT,
                                pageHeight - MARGIN_TOP),
                        Alignment.Left,
                        null);
                // TODO: TOC Entry

                setBlueStroke(contentStream, 70f);
                contentStream.close();

//                        setScoreTranslations(translator);
                Table.TableBuilder dashboardTableBuilder = Table.builder()
                        .font(styles.getTableBodyStyle().getFont())
                        .fontSize(styles.getTableBodyStyle().getFontSize())
                        .borderWidth(0.5f)
                        .borderStyle(BorderStyle.SOLID)
                        .addColumnsOfWidth(tableRowWidth, tableRowWidth, tableRowWidth, tableRowWidth, tableRowWidth, tableRowWidth, tableRowWidth)
                        .addRow(
                                Row.builder()
                                        .font(styles.getTableHeaderStyle().getFont())
                                        .fontSize(styles.getTableHeaderStyle().getFontSize())
                                        .add(TextCell.builder().text(translation.getCatalogueCountry()).build())
                                        .add(TextCell.builder().text(translation.getCatalogueName()).build())
                                        .add(TextCell.builder().text(translation.getAccessibilityAccessUrl()).build())
                                        .add(TextCell.builder().text(translation.getAccessibilityDownloadUrl()).build())
                                        .add(TextCell.builder().text(translation.getDcatApCompliance()).build())
                                        .add(TextCell.builder().text(translation.getMachineReadability()).build())
                                        .add(TextCell.builder().text(translation.getCatalogueRating()).build())

                                        .build());

                // Build rows
                task.getMetrics().forEach(metric -> {
                    JsonObject info = metric.getJsonObject("info");
                    JsonObject accessibility = metric.getJsonObject("accessibility");
                    JsonObject interoperability = metric.getJsonObject("interoperability");

                    dashboardTableBuilder.addRow(
                            Row.builder()
                                    // Catalogue country
                                    .add(TextCell.builder().text("placeholder").build())

                                    // Catalogue name
                                    .add(TextCell.builder().text(getStringValue(info, "title")).build())

                                    // Accessibility Access URL
                                    .add(TextCell.builder().text(getPercentageValue(accessibility, "accessUrlStatusCodes")).build())

                                    // Accessibility Download URL
                                    .add(TextCell.builder().text(getPercentageValue(accessibility, "downloadUrlStatusCodes")).build())

                                    // DCAT AP compliance
                                    .add(TextCell.builder().text(getPercentageValue(interoperability, "dcatApCompliance")).build())

                                    // Machine readability
                                    .add(TextCell.builder().text(getPercentageValue(interoperability, "formatMediaTypeMachineReadable")).build())

                                    // Catalogue Rating
                                    .add(TextCell.builder().text(getScoreValue(metric)).build())
                                    .build());
                });

                Table dashboardTable = dashboardTableBuilder.build();
                RepeatedHeaderTableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder()
                        .startX(MARGIN_LEFT)
                        .startY(pageHeight - 100f)
                        .endY(MARGIN_BOTTOM)
                        .table(dashboardTable)
                        .build();

                if (dashboardTable != null && !dashboardTable.getRows().isEmpty()) {
                    // Draw table
                    tableDrawer.draw(
                            () -> pdDocument,
                            () -> new PDPage(PDRectangle.A4),
                            100f);
                }

                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    private void setScoreTranslations(Translator translator) {
        JsonObject scoreTranslations = new JsonObject()
                .put("good", translation.getScoreGood())
                .put("excellent", translation.getScoreExcellent())
                .put("bad", translation.getScoreBad())
                .put("sufficient", translation.getScoreSufficient());
        translator.getCountries().put("scoreTranslations", scoreTranslations);
    }

    private String getPercentageValue(JsonObject motherObject, String key) {
        JsonArray keyArray = motherObject.getJsonArray(key);
        if (keyArray == null || keyArray.isEmpty()) {
            return "n/a";
        } else {
            if (keyArray.getJsonObject(0).getString("name").equals("yes") || keyArray.getJsonObject(0).getString("name").equals("no")) {
                for (Object item : keyArray) {
                    JsonObject itemObject = (JsonObject) item;
                    if (itemObject.getString("name").equals("yes")) {
                        int percentage = (int) Math
                                .floor(itemObject.getDouble("percentage"));
                        return percentage + "%";
                    }
                }
            } else {
                for (Object item : keyArray) {
                    JsonObject itemObject = (JsonObject) item;
                    if (itemObject.getString("name").equals("200")) {
                        int percentage = (int) Math
                                .floor(itemObject.getDouble("percentage"));
                        return percentage + "%";
                    }
                }
            }
            return "0%";
        }
    }

    private String getStringValue(JsonObject motherObject, String key) {
        return motherObject.getString(key, "-");
    }

    private String getScoreValue(JsonObject motherObject) {
        if (motherObject.containsKey("score")) {
            // Excellent: 351 - 405
            // Good: 221 - 350
            // Sufficient: 121 - 220
            // Bad: 0 - 120
            int score = motherObject.getInteger("score");
            if (score < 121) {
                return translation.getScoreBad();
//                return translator.getCountries().getJsonObject("scoreTranslations").getString("bad");
            } else if (score < 221) {
                return translation.getScoreSufficient();
//                return translator.getCountries().getJsonObject("scoreTranslations").getString("sufficient");
            } else if (score < 351) {
                return translation.getScoreGood();
//                return translator.getCountries().getJsonObject("scoreTranslations").getString("good");
            } else {
                return translation.getScoreExcellent();
//                return translator.getCountries().getJsonObject("scoreTranslations").getString("excellent");
            }
        } else {
            return "-";
        }
    }

    private Paragraph createParagraphFromHTML(String htmlText) throws IOException {
        if (!htmlPattern.matcher(htmlText).find()) {
            Paragraph paragraph = new Paragraph();
            paragraph.addText(htmlText.stripLeading(),
                    styles.getParagraphStyle().getFontSize(),
                    styles.getParagraphStyle().getFont());
            return paragraph;
        } else {
            org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(htmlText);
            GenerateParagraphJSoupNodeVisitor visitor = new GenerateParagraphJSoupNodeVisitor(new Paragraph(), this.styles);
            jsoupDocument.traverse(visitor);
            return visitor.getParagraph();
        }
    }

    private String convertURLsToMarkup(String text) {
        return "{link:none[" + text + "]}" + text + "{link}";
//        return urlPattern
//                .matcher(text)
//                .replaceAll(mr -> "{link:none[" + mr.group() + "]}" + mr.group() + "{link}");
    }

    public Future<Void> save(String path) {
        return vertx.executeBlocking(promise -> {
            try (PDDocument currentDocument = this.pdDocument) {
                log.debug("PdfReport try to save PDF report [{}] ...", path);
                insertFooter();
                // TODO: set document meta data
                //setDocumentSettings();
                currentDocument.save(new File(path));
                currentDocument.close();
                promise.complete();
            } catch (IOException e) {
                log.error("PDF generation exception :", e);
                promise.fail(e);
            }

        }, false);
    }

    private void setDocumentSettings() {
        // TODO: set document meta data
        pdDocument.setVersion(1.6f);
        PDDocumentInformation info = pdDocument.getDocumentInformation();
        info.setAuthor("Author");
        info.setTitle("Title");
        info.setCreator("Creator");
        info.setProducer("Producer");
        info.setSubject("Subject");
        info.setKeywords("Keywords");
    }

    private void insertFooter() throws IOException {
        float footerBoxWidth = pageWidth - MARGIN_LEFT - MARGIN_RIGHT;
        float footerY = 37;

        int pageCount = 1;
        for (PDPage page : pdDocument.getPages()) {
            if (pageCount++ > 1) {
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        pdDocument,
                        page,
                        PDPageContentStream.AppendMode.APPEND,
                        false,
                        true)) {

                    Annotations.HyperlinkAnnotation DataPortalHyperlink = new Annotations.HyperlinkAnnotation(
                            translation.getDashboardTitle(),
                            Annotations.HyperlinkAnnotation.LinkStyle.none);
                    AnnotatedStyledText dataPortal = new AnnotatedStyledText(translation.getDashboardTitle(),
                            styles.getFooterStyle().getFontSize(),
                            styles.getFooterStyle().getFont(),
                            Color.black, 0, Collections.singleton(DataPortalHyperlink));
                    TextFlow textDataPortal = new TextFlow();
                    textDataPortal.setMaxWidth(footerBoxWidth);
                    textDataPortal.add(dataPortal);
                    textDataPortal.drawText(contentStream, new Position(MARGIN_LEFT, footerY), Alignment.Left, null);

                    TextFlow textPageNumber = new TextFlow();
                    textPageNumber.setMaxWidth(footerBoxWidth);
                    textPageNumber.addText(translation.getPageTranslation() + " " + pageCount,
                            styles.getFooterStyle().getFontSize(),
                            styles.getFooterStyle().getFont());
                    textPageNumber.drawText(contentStream, new Position(MARGIN_LEFT, footerY), Alignment.Right, null);
                }
            }
        }
    }
}
