package io.piveau.hub.util;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class ContentNegotiation {

    private final RoutingContext context;
    private final String id;
    private String acceptType;

    private final HttpMethod method;

    private static final Pattern pattern;

    static {
        String regex = "^(.+)(\\.)(" + String.join("|", ContentType.getFileExtensions()) + ")$";
        pattern = Pattern.compile(regex);
    }

    public ContentNegotiation(RoutingContext context){
        this(context, "id", ContentType.DEFAULT);
    }

    public ContentNegotiation(RoutingContext context, String idName, ContentType defaultContentType) {
        this.context = context;
        method = context.request().method();

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        RequestParameter requestId = parameters.pathParameter(idName);
        if (requestId == null) {
            id = "";
            List<MIMEHeader> acceptHeader = context.parsedHeaders().accept();
            if (!acceptHeader.isEmpty() && ContentType.valueOfMimeType(acceptHeader.get(0).value()) != null) {
                acceptType = acceptHeader.get(0).value();
            } else {
                acceptType = defaultContentType.getMimeType();
            }
        } else {
            List<MatchResult> results = pattern.matcher(requestId.getString()).results().toList();

            if (results.isEmpty()) {
                id = requestId.getString();
                List<MIMEHeader> acceptHeader = context.parsedHeaders().accept();
                if (!acceptHeader.isEmpty() && ContentType.valueOfMimeType(acceptHeader.get(0).value()) != null) {
                    acceptType = acceptHeader.get(0).value();
                } else {
                    acceptType = defaultContentType.getMimeType();
                }
            } else {
                id = results.get(0).group(1);
                String ext = results.get(0).group(3);
                acceptType = ContentType.valueOfFileExtension(ext).getMimeType();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public void headOrGetResponse(String content) {
        if (method == HttpMethod.HEAD) {
            context.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, acceptType)
                    .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.getBytes().length))
                    .end();
        } else {
            context.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, acceptType)
                    .end(content);
        }
    }
}
