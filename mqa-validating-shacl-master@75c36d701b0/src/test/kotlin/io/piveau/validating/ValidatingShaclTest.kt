package io.piveau.validating

import io.piveau.vocabularies.readTurtleResource
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.system.measureTimeMillis

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidatingShaclTest {

    @Test
    fun `Validate a dataset 10 times and measure the durations`() {
        ModelFactory.createDefaultModel().apply {
            readTurtleResource("test.ttl")
            RDFDataMgr.write(System.out, validateModel(this@apply, "dcatap211level1"), Lang.TTL)
            var sum = 0L
            repeat(10) {
                val milli = measureTimeMillis {
                    validateModel(this@apply, "dcatap211level1")
                }
                println("Validation jena took $milli milliseconds")
                sum += milli
            }
            println("Average is ${sum/10} milliseconds")
        }
    }

}