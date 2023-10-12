package io.piveau.metrics

import io.piveau.vocabularies.readTurtleResource
import io.piveau.vocabularies.vocabulary.DQV
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import java.io.InputStream

object PiveauDQVVocabulary {
    val model: Model by lazy {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("piveau-dqv-vocabulary.ttl")
        }
    }

    fun loadScoreValues(scoreValues: InputStream) {
        RDFDataMgr.read(model, scoreValues, Lang.TURTLE)
    }

    fun metric(uri: String): Resource = model.getResource(uri)

    fun dimensionOf(metric: Resource): Resource? = model.getResource(metric.uri).getPropertyResourceValue(DQV.inDimension)

}
