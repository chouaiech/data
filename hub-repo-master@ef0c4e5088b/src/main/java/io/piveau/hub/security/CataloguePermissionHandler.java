package io.piveau.hub.security;

import io.piveau.security.PiveauAuth;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class CataloguePermissionHandler implements Handler<RoutingContext> {

    private final List<String> scopes;

    public CataloguePermissionHandler(List<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public void handle(RoutingContext context) {

        if (context.user().principal().containsKey("resources")) {
            List<String> resources = context.user().principal().getJsonArray("resources").stream()
                    .map(Object::toString).toList();
            if (resources.contains("*")) {
                context.next();
            } else {
                context.fail(403);
            }
        } else if (PiveauAuth.userHasRole(context.user(), "operator")
                || scopes.stream().allMatch(scope -> PiveauAuth.userHasPermission(context.user(), "Catalogue Resource", scope))) {
            context.next();
        } else {
            context.fail(403);
        }
    }

}
