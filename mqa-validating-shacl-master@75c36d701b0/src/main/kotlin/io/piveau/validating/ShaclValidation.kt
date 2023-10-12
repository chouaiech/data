package io.piveau.validating

import org.apache.jena.rdf.model.Model
import org.apache.jena.shacl.ShaclValidator

var useJena: Boolean = true

const val DEFAULT_SHAPES_VERSION: String = "dcatap211level1"

private val validator = ShaclValidator.get()

fun validateModel(model: Model, shapesVersion: String = DEFAULT_SHAPES_VERSION): Model {
    when (shapesVersion) {
        "dcatap211level1",
        "dcatap211level2",
        "dcatap210level1" -> {}

        else -> model.add(Vocabularies.model)
    }
    val shapes = shapesVersion.findShapes()
    val report = validator.validate(shapes, model.graph)
    return report.model
}
