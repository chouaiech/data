package io.piveau.hub.shell

import io.vertx.ext.shell.command.CommandProcess

fun extractCatalogueList(catalogueList: List<String>, process: CommandProcess): List<String> {
    val catalogueId = process.commandLine().getArgumentValue<String>(0)
    return if (catalogueId == null) {

        var offset = process.commandLine().getOptionValue<String>("offset").toInt()
        var limit = process.commandLine().getOptionValue<String>("limit").toInt()

        offset = when {
            offset > catalogueList.size - 1 -> catalogueList.size - 1
            else -> offset
        }
        limit = when {
            offset + limit > catalogueList.size -> catalogueList.size - offset
            else -> limit
        }
        catalogueList.subList(offset, offset + limit)

    } else listOf(catalogueId)
}
