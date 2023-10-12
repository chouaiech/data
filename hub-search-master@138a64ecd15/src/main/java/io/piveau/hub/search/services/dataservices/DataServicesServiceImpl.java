package io.piveau.hub.search.services.dataservices;

import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

public class DataServicesServiceImpl implements DataServicesService {

    private final SearchClient searchClient;

    private final PiveauContext serviceContext;

    DataServicesServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager,
                            Handler<AsyncResult<DataServicesService>> handler) {
        this.searchClient = SearchClient.build(vertx, config, indexManager);
        this.serviceContext = new PiveauContext("hub.search", "DataServicesService");
        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<JsonObject> createDataService(JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend("id-not-available");
        searchClient.postDocument("dataservice", false, payload).onSuccess(result -> {
            resourceContext.log().debug("Post success: " + payload);
            promise.complete(ReturnHelper.returnSuccess(201, new JsonObject().put("id", result)));
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> createOrUpdateDataService(String dataServiceId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(dataServiceId);
        searchClient.putDocument("dataservice", dataServiceId, false, payload).onSuccess(result -> {
            resourceContext.log().debug("Put success: " + payload);
            if (result == 200) {
                // updated
                resourceContext.log().debug("Update dataservice: DataService {} updated.", dataServiceId);
            } else {
                // created
                resourceContext.log().debug("Create dataservice: DataService {} created.", dataServiceId);
            }
            promise.complete(ReturnHelper.returnSuccess(result, new JsonObject().put("id", dataServiceId)));
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> modifyDataService(String dataServiceId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(dataServiceId);
        searchClient.patchDocument("dataservice", dataServiceId, false, payload).onSuccess(result -> {
            resourceContext.log().debug("Patch success: " + payload);
            promise.complete(ReturnHelper.returnSuccess(200, dataServiceId));
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject>  readDataService(String dataServiceId) {
        Promise<JsonObject> promise = Promise.promise();
        searchClient.getDocument("dataservice", dataServiceId, false)
                .onSuccess(result -> promise.complete(ReturnHelper.returnSuccess(200, result)))
                .onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> deleteDataService(String dataServiceId) {
        Promise<JsonObject> promise = Promise.promise();
        searchClient.deleteDocument("dataservice", dataServiceId, false)
                .onSuccess(result -> promise.complete(
                        ReturnHelper.returnSuccess(200, new JsonObject().put("id", dataServiceId))))
                .onFailure(promise::fail);
        return promise.future();
    }

}
