package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.vertx.core.json.JsonArray;

public class ScriptBuilder {

    public static String buildScript(String replacementKey, JsonArray replacements) {
        StringBuilder script = new StringBuilder();
        script.append("void replace(def current, def replacementKey, def params) {\n");
        for (Object obj : replacements) {
            String replacement = (String) obj;
            String[] split = replacement.split(":");

            String key = split[0];
            String valueName = split[1];

            script.append("  current.put(\"");
            script.append(key);
            script.append("\", params.vocab.get(replacementKey).");
            script.append(valueName);
            script.append(");");
            script.append("\n");
        }
        script.append("}\n");
        script.append("\n");
        script.append("void visitAllFields(def current, def params) {\n");
        script.append("  if (current instanceof List) {\n");
        script.append("    for (def child: current) {\n");
        script.append("      visitAllFields(child, params);\n");
        script.append("    }\n");
        script.append("  }\n");
        script.append("  if (!(current instanceof Map)) {\n");
        script.append("    return;\n");
        script.append("  }\n");
        script.append("  for (def key: current.keySet()) {\n");
        script.append("    if (params.fields.contains(key)) {\n");
        script.append("      def child = current.get(key);\n");
        script.append("      if (child instanceof List) {\n");
        script.append("        for (def listItem: child) {\n");
        script.append("          def replacementKey = listItem.get(\"").append(replacementKey).append("\");\n");
        script.append("          replace(listItem, replacementKey, params);\n");
        script.append("        }\n");
        script.append("      }\n");
        script.append("      if (child instanceof Map) {\n");
        script.append("        def replacementKey = child.get(\"").append(replacementKey).append("\");\n");
        script.append("        replace(child, replacementKey, params);\n");
        script.append("      }\n");
        script.append("    }\n");
        script.append("  }\n");
        script.append("  for (def child: current.values()) {\n");
        script.append("    visitAllFields(child, params);\n");
        script.append("  }\n");
        script.append("}\n");
        script.append("\n");
        script.append("visitAllFields(ctx, params);");
        return script.toString();
    }
}
