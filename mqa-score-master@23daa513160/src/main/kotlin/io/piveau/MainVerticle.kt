package io.piveau

import io.piveau.pipe.connector.PipeConnector
import io.piveau.metrics.MetricsScoreVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Launcher
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.awaitResult

class MainVerticle : CoroutineVerticle() {

    override suspend fun start() {
        vertx.deployVerticle(MetricsScoreVerticle::class.java, DeploymentOptions()).await()
        PipeConnector.create(vertx).await().publishTo(MetricsScoreVerticle.ADDRESS)
    }

}

fun main(args: Array<String>) {
    Launcher.executeCommand("run", *(args.plus(MainVerticle::class.java.name)))
}
