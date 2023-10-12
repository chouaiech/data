package io.piveau.hub.security;

import io.piveau.security.PiveauAuth;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.List;

public class DatasetPermissionHandler implements Handler<RoutingContext> {

    private final List<String> scopes;

    public DatasetPermissionHandler(List<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public void handle(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final String resource;
        if (parameters.pathParametersNames().contains("catalogueId")) {
            resource = parameters.pathParameter("catalogueId").getString();
        } else if (parameters.queryParameter("catalogue") != null) {
            resource = parameters.queryParameter("catalogue").getString();
        } else if (context.get("catalogueId") != null) {
            resource = context.get("catalogueId");
        } else if (context.queryParams().contains("catalogue")) {
            resource = context.queryParams().get("catalogue");
        } else {
            context.fail(403);
            return;
        }

        if (context.user().principal().containsKey("resources")) {
            List<String> resources = context.user().principal().getJsonArray("resources").stream()
                    .map(Object::toString).toList();
            if (resources.contains("*") || resources.contains(resource)) {
                context.next();
            } else {
                context.fail(403);
            }
        } else if (PiveauAuth.userHasRole(context.user(), "operator")
                || scopes.stream().allMatch(scope -> PiveauAuth.userHasPermission(context.user(), resource, scope))) {
            context.next();
        } else {
            context.fail(403);
        }
    }

}
