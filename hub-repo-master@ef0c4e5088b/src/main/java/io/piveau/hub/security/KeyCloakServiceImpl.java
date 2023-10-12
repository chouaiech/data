package io.piveau.hub.security;

import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.piveau.security.KeycloakResourceHelper;
import io.piveau.security.KeycloakTokenServerConfig;
import io.piveau.security.PiveauAuth;
import io.piveau.security.PiveauAuthConfig;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class KeyCloakServiceImpl implements KeyCloakService {

    private final PiveauContext serviceContext;

    private final PiveauAuthConfig piveauAuthConfig;

    private PiveauAuth piveauAuth;
    private KeycloakResourceHelper keycloakResourceHelper;

    public KeyCloakServiceImpl(Vertx vertx, JsonObject jsonConfig, Handler<AsyncResult<KeyCloakService>> readyHandler) {
        serviceContext = new PiveauContext("hub-repo", "keycloakService");

        JsonObject authorizationProcessData = ConfigHelper.forConfig(jsonConfig)
                .forceJsonObject(Constants.ENV_PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA);

        piveauAuthConfig = new PiveauAuthConfig(authorizationProcessData);
        if (piveauAuthConfig.getTokenServerConfig() instanceof KeycloakTokenServerConfig) {
            keycloakResourceHelper = new KeycloakResourceHelper(WebClient.create(vertx),
                    (KeycloakTokenServerConfig) piveauAuthConfig.getTokenServerConfig());

            Future<PiveauAuth> initAuthFuture = PiveauAuth.create(vertx, piveauAuthConfig);

            initAuthFuture.onSuccess(piveauAuthResult -> {
                piveauAuth = piveauAuthResult;
                readyHandler.handle(Future.succeededFuture(this));
            }).onFailure(cause -> {
                PiveauContext resourceContext = serviceContext.extend("init auth");
                resourceContext.log().error(cause.getMessage());
                readyHandler.handle(Future.succeededFuture(this));
            });
        } else {
            PiveauContext resourceContext = serviceContext.extend("init auth");
            resourceContext.log().info("Keycloak Service deployed");
            readyHandler.handle(Future.succeededFuture(this));
        }
    }

    @Override
    public KeyCloakService createResource(String catalogueId) {
        PiveauContext resourceContext = serviceContext.extend(catalogueId);

        JsonArray scopes = new JsonArray()
                .add(Constants.KEYCLOAK_SCOPE_DATASET_CREATE)
                .add(Constants.KEYCLOAK_SCOPE_DATASET_UPDATE)
                .add(Constants.KEYCLOAK_SCOPE_DATASET_DELETE);

        if (piveauAuth != null) {
            piveauAuth.requestClientToken().onSuccess(token -> {
                if (keycloakResourceHelper != null) {
                    keycloakResourceHelper.existsGroup(token, catalogueId).onSuccess(existsGroupResult -> {
                        if (Boolean.FALSE.equals(existsGroupResult)) {
                            keycloakResourceHelper.createGroup(token, catalogueId)
                                    .compose(createGroupResult ->
                                            keycloakResourceHelper.createResource(
                                                    token,
                                                    catalogueId,
                                                    "catalogue",
                                                    catalogueId,
                                                    piveauAuthConfig.getClientId(),
                                                    true,
                                                    scopes))
                                    .onSuccess(createResourceResult -> keycloakResourceHelper.createGroupPolicy(token,
                                            catalogueId, catalogueId, scopes))
                                    .onFailure(cause -> resourceContext.log().error(cause.getMessage()));
                        }
                    })
                    .onFailure(cause -> resourceContext.log().error(cause.getMessage()));
                }
            }).onFailure(cause -> resourceContext.log().error(cause.getMessage()));
        }

        return this;
    }

    @Override
    public KeyCloakService deleteResource(String catalogueId) {
        PiveauContext resourceContext = serviceContext.extend(catalogueId);

        if (piveauAuth != null) {
            piveauAuth.requestClientToken().onSuccess(token -> {
                if (keycloakResourceHelper != null) {
                    keycloakResourceHelper.deleteGroup(token, catalogueId)
                            .onFailure(cause -> resourceContext.log().error(cause.getMessage()));
                    keycloakResourceHelper.deleteResource(token, catalogueId)
                            .onFailure(cause -> resourceContext.log().error(cause.getMessage()));
                }
            }).onFailure(cause -> resourceContext.log().error(cause.getMessage()));
        }

        return this;
    }

}
