package io.piveau.metrics.reporter.pdf;

import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

class Style {
    private final int fontSize;
    private final PDFont font;

    public Style(PDFont font, int fontSize) {
        this.font = font;
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public PDFont getFont() {
        return font;
    }
}


public class Styles {

    private final TrueTypeFont robotoRegularTTFFile;
    private final TrueTypeFont robotoBoldTTFFile;
    private final TrueTypeFont robotoItalicTTFFile;
    private final TrueTypeFont robotoItalicBoldTTFFile;

    private PDFont robotoRegular;
    private PDFont robotoBold;
    private PDFont robotoItalic;
    private PDFont robotoItalicBold;

    private Style headingOneStyle;
    private Style headingTwoStyle;
    private Style headingThreeStyle;
    private Style headingFourStyle;
    private Style paragraphStyle;
    private Style boldParagraphStyle;
    private Style tableHeaderStyle;
    private Style tableBodyStyle;
    private Style footerStyle;

    public Styles(TrueTypeFont robotoRegularTTFFile,
                  TrueTypeFont robotoBoldTTFFile,
                  TrueTypeFont robotoItalicTTFFile,
                  TrueTypeFont robotoItalicBoldTTFFile) {
        this.robotoRegularTTFFile = robotoRegularTTFFile;
        this.robotoBoldTTFFile = robotoBoldTTFFile;
        this.robotoItalicTTFFile = robotoItalicTTFFile;
        this.robotoItalicBoldTTFFile = robotoItalicBoldTTFFile;
    }

    public void initializeFonts(PDDocument pdDocument) throws IOException {
        robotoRegular = PDType0Font.load(pdDocument, robotoRegularTTFFile, true);
        robotoBold = PDType0Font.load(pdDocument, robotoBoldTTFFile, true);
        robotoItalic = PDType0Font.load(pdDocument, robotoItalicTTFFile, true);
        robotoItalicBold = PDType0Font.load(pdDocument, robotoItalicBoldTTFFile, true);

        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
        PDFont fontBoldItalic = PDType1Font.HELVETICA_BOLD_OBLIQUE;
        PDFont fontMono = PDType1Font.COURIER;

        headingOneStyle = new Style(fontPlain, 18);
        headingTwoStyle = new Style(fontPlain, 16);
        headingThreeStyle = new Style(fontBold, 14);
        headingFourStyle = new Style(fontPlain, 14);
        paragraphStyle = new Style(fontPlain, 12);
        boldParagraphStyle = new Style(fontBold, 12);
        tableHeaderStyle = new Style(fontBold, 10);
        tableBodyStyle = new Style(fontPlain, 10);
        footerStyle = new Style(fontItalic, 10);
    }

    public Style getHeadingOneStyle() {
        return headingOneStyle;
    }

    public Style getHeadingTwoStyle() {
        return headingTwoStyle;
    }

    public Style getHeadingThreeStyle() {
        return headingThreeStyle;
    }

    public Style getHeadingFourStyle() {
        return headingFourStyle;
    }

    public Style getParagraphStyle() {
        return paragraphStyle;
    }

    public Style getBoldParagraphStyle() {
        return boldParagraphStyle;
    }

    public Style getTableHeaderStyle() {
        return tableHeaderStyle;
    }

    public Style getTableBodyStyle() {
        return tableBodyStyle;
    }

    public Style getFooterStyle() {
        return footerStyle;
    }

    public PDFont getRobotoRegular() {
        return this.robotoRegular;
    }

    public PDFont getRobotoBold() {
        return this.robotoBold;
    }

    public PDFont getRobotoItalic() {
        return this.robotoItalic;
    }

    public PDFont getRobotoItalicBold() {
        return this.robotoItalicBold;
    }
}
