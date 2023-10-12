package io.piveau.metrics.cache.dqv.sparql;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryCollection {

    private QueryCollection(){
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, String> queryMap = new HashMap<>();

    static public void init(Vertx vertx, String path) {
        queryMap.clear();
        List<String> queries = vertx.fileSystem().readDirBlocking(path, "([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.sparql)$");
        queries.forEach(file -> {
            Buffer query = vertx.fileSystem().readFileBlocking(file);
            String name = Path.of(file).getFileName().toString();
            queryMap.put(name, query.toString().replaceAll("\n", "\n "));
        });
    }

    public static Set<String> listQueries() {
        return queryMap.keySet();
    }

    public static String getQuery(String name) {
        return queryMap.getOrDefault(name + ".sparql", "");
    }

}
