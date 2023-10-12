package io.piveau.metrics.reporter.pdf

import io.piveau.metrics.reporter.ApplicationConfig
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.apache.fontbox.ttf.TTFParser
import org.slf4j.LoggerFactory
import rst.pdfbox.layout.elements.ImageElement
import java.io.ByteArrayInputStream
import java.io.IOException

class PdfReportBuilder(vertx: Vertx, private val config: JsonObject) {
    private val log = LoggerFactory.getLogger(javaClass)

    private lateinit var headerImage: ImageElement
    private lateinit var styles: Styles

    init {
        try {
            val imageBuffer = vertx.fileSystem().readFileBlocking(config.getString("headerFilename", "report_header.png"))

            headerImage = ImageElement(ByteArrayInputStream(imageBuffer.bytes))

            headerImage.width = ImageElement.SCALE_TO_RESPECT_WIDTH
            headerImage.height = ImageElement.SCALE_TO_RESPECT_WIDTH

            val parser = TTFParser(true)

            var buffer = vertx.fileSystem().readFileBlocking("fonts/" + ApplicationConfig.DEFAULT_FONT_REGULAR)
            val robotoRegularTTFFile = ByteArrayInputStream(buffer.bytes).use { parser.parseEmbedded(it) }
            buffer = vertx.fileSystem().readFileBlocking("fonts/" + ApplicationConfig.DEFAULT_FONT_BOLD)
            val robotoBoldTTFFile = ByteArrayInputStream(buffer.bytes).use { parser.parseEmbedded(it) }
            buffer = vertx.fileSystem().readFileBlocking("fonts/" + ApplicationConfig.DEFAULT_FONT_ITALIC)
            val robotoItalicTTFFile = ByteArrayInputStream(buffer.bytes).use { parser.parseEmbedded(it) }
            buffer = vertx.fileSystem().readFileBlocking("fonts/" + ApplicationConfig.DEFAULT_FONT_BOLD_ITALIC)
            val robotoBoldItalicTTFFile = ByteArrayInputStream(buffer.bytes).use { parser.parseEmbedded(it) }

            styles = Styles(
                robotoRegularTTFFile,
                robotoBoldTTFFile,
                robotoItalicTTFFile,
                robotoBoldItalicTTFFile
            )
        } catch (e: IOException) {
            log.error("Initializing pdf report builder", e)
        }
    }

    fun createGenerator(): PdfGenerator {
        return PdfGenerator(config, headerImage, styles)
    }

}