@file:JvmName("HubRepo")

package io.piveau

import io.piveau.dcatap.TripleStoreException
import io.piveau.rdf.RDFMimeTypes
import io.vertx.ext.web.RoutingContext
import io.vertx.serviceproxy.ServiceException

fun failureResponse(routingContext: RoutingContext, cause: Throwable) = when (cause) {
    is ServiceException -> routingContext.fail(cause.failureCode(), cause)
    is TripleStoreException -> when (cause.code) {
        404 -> routingContext.fail(404, cause)
        else -> routingContext.fail(cause)
    }
    else -> routingContext.fail(cause)
}

// Useful for junit tests
enum class RDFMimeTypesEnum(val value: String) {
    JSONLD(RDFMimeTypes.JSONLD),
    RDFXML(RDFMimeTypes.RDFXML),
    NTRIPLES(RDFMimeTypes.NTRIPLES),
    NQUADS(RDFMimeTypes.NQUADS),
    TRIG(RDFMimeTypes.TRIG),
    TRIX(RDFMimeTypes.TRIX),
    TURTLE(RDFMimeTypes.TURTLE),
    N3(RDFMimeTypes.N3)
}

enum class ValueTypesEnum {
    uriRefs, identifiers, originalIds, metadata
}
