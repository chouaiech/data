package io.piveau.metrics.reporter.pdf;

import io.piveau.metrics.reporter.CatalogueMetrics;
import io.piveau.metrics.reporter.model.Translation;
import io.piveau.metrics.reporter.model.chart.Chart;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;
import rst.pdfbox.layout.elements.*;
import rst.pdfbox.layout.text.*;
import rst.pdfbox.layout.text.annotations.AnnotatedStyledText;
import rst.pdfbox.layout.text.annotations.Annotations;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class PdfGenerator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final float LINE_SPACING = 1.45f;
    private static final Color lineColor = new Color(114, 214, 234);
    private static final float MARGIN_LEFT = 36;
    private static final float MARGIN_RIGHT = 40;
    private static final float MARGIN_TOP = 41;
    private static final float MARGIN_BOTTOM = 50;

    private final float pageWidth;
    private final float pageHeight;

    // PDF Layout Document
    private final Document document;

    private final String portalUrl;
    private final String shaclUrl;
    private final String methodologyUrl;

    private final NewLine newLine = new NewLine();

    private final Pattern htmlPattern = Pattern.compile("</?[a-zA-Z]+>");
//    private final Pattern urlPattern = Pattern.compile(
//            "((http:|https:)//)?(([a-zA-Z])+)(\\.(([a-zA-Z])+))+/?((([a-zA-Z])+)(\\.(([a-zA-Z])+))*/?)*",
//            Pattern.CASE_INSENSITIVE);

    private final Styles styles;

    public PdfGenerator(JsonObject config, ImageElement headerImage, Styles styles) {
        document = new Document(
                MARGIN_LEFT,
                MARGIN_RIGHT,
                MARGIN_TOP,
                MARGIN_BOTTOM);

        pageWidth = document.getPageFormat().getMediaBox().getWidth();
        pageHeight = document.getPageFormat().getMediaBox().getHeight();

        portalUrl = config.getString("portalUrl", "");
        shaclUrl = config.getString("shaclUrl", "");
        methodologyUrl = config.getString("methodologyUrl", "");

        this.styles = styles;

        document.add(headerImage);

        try {
            styles.initializeFonts(document.getPDDocument());
        } catch (IOException e) {
            log.error("Initializing fonts in document", e);
        }
    }

    public void render(CatalogueMetrics catalogueMetrics, Translation translation) {
        renderTitlePage(translation);
        renderMethodologyPage(translation);
        renderData(catalogueMetrics);
        renderFooter(translation);
    }

    private void renderTitlePage(Translation translation) {
        try {
            Paragraph titleParagraph = new Paragraph();
            titleParagraph.setAlignment(Alignment.Center);
            titleParagraph.add(newLine);

            titleParagraph.addMarkup(
//                    urlPattern.matcher(portalUrl).replaceAll(mr -> "{link:none[" + mr.group() + "]}" + mr.group() + "{link}"),
                    "{link:none[" + portalUrl + "]}" + portalUrl + "{link}",
                    styles.getHeadingTwoStyle().getFontSize(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont(),
                    styles.getHeadingTwoStyle().getFont());

            titleParagraph.add(newLine);
            titleParagraph.add(newLine);

            titleParagraph.addText(translation.getMqaTitle().trim(),
                    styles.getHeadingOneStyle().getFontSize(),
                    styles.getHeadingOneStyle().getFont());
            titleParagraph.add(newLine);
            titleParagraph.add(newLine);

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, translation.getLanguage());
            titleParagraph.addText(dateFormat.format(new Date()),
                    styles.getParagraphStyle().getFontSize(),
                    styles.getParagraphStyle().getFont());

            document.add(titleParagraph);
        } catch (IOException e) {
            log.error("Add title to pdf", e);
        }
    }

    private void renderMethodologyPage(Translation translation) {
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

            Paragraph methodologyIntroTextParagraph = createParagraphFromHTML(translation.getMqaIntro().trim());
            methodologyIntroTextParagraph.setLineSpacing(LINE_SPACING);
            document.add(methodologyIntroTextParagraph);
            document.add(new VerticalSpacer(19));

            String mqaLink = "<a href=\""
                    + methodologyUrl
                    + "\" target=\"_blank\">"
                    + translation.getMethodologyHeadline()
                    + "</a>";
            Paragraph methodologyLink = createParagraphFromHTML(mqaLink);
            document.add(methodologyLink);
            document.add(new VerticalSpacer(35));

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

            String shaclLink = "<a href=\""
                    + shaclUrl
                    + "\" target=\"_blank\">"
                    + translation.getMethodologyCoverLinkText()
                    + "</a>";
            Paragraph methodologyCoverLink = createParagraphFromHTML(shaclLink);
            document.add(methodologyCoverLink);
            document.add(new VerticalSpacer(35));

            Paragraph methodologyAssumptionsHeadline = new Paragraph();
            methodologyAssumptionsHeadline.addText(translation.getMethodologyAssumptionsHeadline().trim(),
                    styles.getHeadingThreeStyle().getFontSize(),
                    styles.getHeadingThreeStyle().getFont());
            document.add(methodologyAssumptionsHeadline);
            // TODO: Add TOC Entry

            Paragraph methodologyAssumptionsMainText = createParagraphFromHTML(translation.getMethodologyAssumptionsMainText().trim());
            methodologyAssumptionsMainText.setLineSpacing(LINE_SPACING);
            document.add(methodologyAssumptionsMainText);

//            changeDocumentState();
        } catch (IOException e) {
            log.error("Rendering methodology page", e);
        }
    }

    private void renderData(CatalogueMetrics catalogueMetrics) {
        renderDimensionPage(
                catalogueMetrics.getCatalogueTitle(),
                "Accessibility",
                catalogueMetrics.getAccessibilityCharts());

        renderDimensionPage(
                catalogueMetrics.getCatalogueTitle(),
                "Contextuality",
                catalogueMetrics.getContextualityCharts());

        renderDimensionPage(
                catalogueMetrics.getCatalogueTitle(),
                "Findability",
                catalogueMetrics.getFindabilityCharts());

        renderDimensionPage(
                catalogueMetrics.getCatalogueTitle(),
                "Interoperability",
                catalogueMetrics.getInteroperabilityCharts());

        renderDimensionPage(
                catalogueMetrics.getCatalogueTitle(),
                "Reusability",
                catalogueMetrics.getReusabilityCharts());
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

    private void renderFooter(Translation translation) {
        float footerBoxWidth = pageWidth - MARGIN_LEFT - MARGIN_RIGHT;
        float footerY = 37;

        int pageCount = 1;

        PDDocument pdDocument = document.getPDDocument();
        for (PDPage page : pdDocument.getPages()) {
            if (pageCount++ > 1) {
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        pdDocument,
                        page,
                        PDPageContentStream.AppendMode.APPEND,
                        false,
                        true)) {

                    Annotations.HyperlinkAnnotation dataPortalHyperlink = new Annotations.HyperlinkAnnotation(
                            translation.getDashboardTitle(),
                            Annotations.HyperlinkAnnotation.LinkStyle.none);
                    AnnotatedStyledText dataPortal = new AnnotatedStyledText(translation.getDashboardTitle(),
                            styles.getFooterStyle().getFontSize(),
                            styles.getFooterStyle().getFont(),
                            Color.black, 0, Collections.singleton(dataPortalHyperlink));
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
                } catch (IOException e) {
                    log.error("Rendering footers", e);
                }
            }
        }
    }

    private void renderDimensionPage(String catalogueTitle, String dimensionTitle, List<Chart> charts) {
        PDDocument pdDocument = document.getPDDocument();

        PDPage page = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page);
        try {
            try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page)) {

                //Heading
                TextFlow title = TextFlowUtil.createTextFlow(catalogueTitle,
                        styles.getHeadingThreeStyle().getFontSize(),
                        styles.getHeadingThreeStyle().getFont());
                title.drawText(contentStream, new Position(MARGIN_LEFT, pageHeight - 42f), Alignment.Left, null);

                TextFlow sectionHeading = TextFlowUtil.createTextFlow(dimensionTitle,
                        styles.getHeadingTwoStyle().getFontSize(),
                        styles.getHeadingTwoStyle().getFont());
                sectionHeading.drawText(contentStream, new Position(MARGIN_LEFT, pageHeight - 72f), Alignment.Left, null);
                //TODO: Add TOC Entry: catalogue.getTitle() + " - " + section.getSectionHeading()

                // Line
                contentStream.setLineWidth(1.5f);
                contentStream.setStrokingColor(lineColor);
                contentStream.moveTo(MARGIN_LEFT, pageHeight - 100f);
                contentStream.lineTo(pageWidth - MARGIN_RIGHT, pageHeight - 100f);
                contentStream.stroke();
            }

            CatalogueTableBuilder catalogueTableBuilder =
                    new CatalogueTableBuilder(pdDocument, styles, (pageWidth - MARGIN_RIGHT - MARGIN_LEFT) / 2);

            for (Chart chart : charts) {
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
        } catch (IOException e) {
            log.error("Rendering dimension page", e);
        }
    }

    public void save(String path) {
        try {
            document.save(new File(path));
        } catch (IOException e) {
            log.error("Saving document", e);
        }
    }

    public Buffer toBuffer() {
        Buffer buffer = Buffer.buffer();
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                buffer.appendByte((byte)(b & 0xFF));
            }
            @Override
            public void write(@NotNull byte[] b) {
                buffer.appendBytes(b);
            }
            @Override
            public void write(@NotNull byte[] b, int off, int len) {
                buffer.appendBytes(b, off, len);
            }
        };

        try {
            document.save(outputStream);
        } catch (IOException e) {
            log.error("Saving document", e);
        }
        return buffer;
    }

}
