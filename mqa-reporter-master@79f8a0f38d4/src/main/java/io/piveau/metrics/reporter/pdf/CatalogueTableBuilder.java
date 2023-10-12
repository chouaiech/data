package io.piveau.metrics.reporter.pdf;

import io.piveau.metrics.reporter.model.chart.Chart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;

public class CatalogueTableBuilder {
    private final PDDocument pdDocument;
    private final Styles styles;
    private final Table.TableBuilder chartTableBuilder;

    private TextCell cellHeadingLeft = null;
    private TextCell cellHeadingRight = null;
    private AbstractCell cellChartLeft = null;
    private AbstractCell cellChartRight = null;

    public CatalogueTableBuilder(PDDocument pdDocument, Styles styles, float tableCellWidth) {
        this.pdDocument = pdDocument;
        this.styles = styles;
        this.chartTableBuilder = Table.builder()
                .addColumnsOfWidth(tableCellWidth, tableCellWidth)
                .horizontalAlignment(HorizontalAlignment.CENTER);
    }

    public void addChart(Chart chart) throws IOException {
        if (cellHeadingLeft == null) {
            cellHeadingLeft = createHeadingCell(chart);
            cellChartLeft = createChartCell(chart);

        } else {
            cellHeadingRight = createHeadingCell(chart);
            cellChartRight = createChartCell(chart);

            chartTableBuilder
                    .addRow(
                            Row.builder()
                                    .font(styles.getBoldParagraphStyle().getFont())
                                    .fontSize(styles.getBoldParagraphStyle().getFontSize())
                                    .add(cellHeadingLeft)
                                    .add(cellHeadingRight)
                                    .build())
                    .addRow(
                            Row.builder()
                                    .font(styles.getParagraphStyle().getFont())
                                    .fontSize(styles.getParagraphStyle().getFontSize())
                                    .add(cellChartLeft)
                                    .add(cellChartRight)
                                    .build());

            clearAllCellMembers();
        }
    }

    private TextCell createHeadingCell(Chart chart) {
        return TextCell.builder()
                .text(chart.getName())
                .padding(8)
                .build();
    }

    private AbstractCell createChartCell(Chart chart) throws IOException {
        AbstractCell chartCell;

        if (chart.getErrorMessage() == null) {
            // The loading of the image into the pdDocument is the performance bottleneck. Caching for the global report is not possible,
            // because the image is loaded into a specific document and can not be reused
            PDImageXObject image = PDImageXObject.createFromByteArray(pdDocument, chart.getImageBytes(), "");
            chartCell = ImageCell.builder().image(image).build();
        } else {
            chartCell = TextCell.builder()
                    .text(chart.getErrorMessage())
                    .padding(8)
                    .build();
        }

        return chartCell;
    }

    private void clearAllCellMembers() {
        cellHeadingLeft = null;
        cellChartLeft = null;
        cellHeadingRight = null;
        cellChartRight = null;
    }

    static class EmptyChart extends Chart {
        public EmptyChart() {
            super("", "");
        }
    }

    public Table getChartTable() throws IOException {
        if (cellHeadingLeft != null && cellHeadingRight == null) {
            addChart(new EmptyChart());
        }
        return chartTableBuilder.build();
    }
}
