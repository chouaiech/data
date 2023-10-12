package io.piveau.dataupload;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.MongoClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DBHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MongoClient mongoClient;
    private final String dbName;


    public DBHandler(MongoClient mongoClient, String dbName) {
        this.mongoClient = mongoClient;
        this.dbName = dbName;
    }

    public Future<String> prepareEntry(JsonArray jsonArray) {

        Promise<String> promise = Promise.promise();

        List<BulkOperation> jsonList = new ArrayList<>();
        for (Object object : jsonArray) {
            BulkOperation bulkOperation = BulkOperation.createInsert((JsonObject) object);
            jsonList.add(bulkOperation);
        }

        mongoClient.bulkWrite(dbName, jsonList, res -> {
            if (res.succeeded()) {
                promise.complete("success");
            } else {
                promise.fail(res.cause());
            }
        });

        return promise.future();
    }

    public Future<String> createEntry(byte[] bytes, String filename, String uuid, String token) {
        Promise<String> promise = Promise.promise();

        JsonObject queryDocument = new JsonObject()
                .put("token", token)
                .put("id", uuid);

        mongoClient.find(dbName, queryDocument)
                .onSuccess(list -> {
                    if (!list.isEmpty()) {
                        JsonObject document = new JsonObject()
                                .put("id", uuid)
                                .put("binaryData", bytes)
                                .put("fileName", filename);

                        deleteEntryByToken(uuid)
                                .onSuccess(status -> {
                                    if (status.equals("Deleting complete")) {
                                        InsertDocument(document)
                                                .onSuccess(result -> promise.complete("success"))
                                                .onFailure(promise::fail);
                                    } else {
                                        log.info("Failed at delete with status: " + status);
                                        promise.fail(status);
                                    }
                                })
                                .onFailure(promise::fail);
                    } else {
                        promise.fail("No document found");
                    }
                })
                .onFailure(promise::fail);

        return promise.future();
    }

    private Future<String> InsertDocument(JsonObject document) {
        Promise<String> promise = Promise.promise();

        mongoClient.insert(dbName, document, resInsertion -> {
            if (resInsertion.succeeded()) {
                promise.complete("success");
            } else {
                promise.fail(resInsertion.cause().toString());
            }
        });

        return promise.future();
    }

    private Future<String> deleteEntryByToken(String token) {
        Promise<String> promise = Promise.promise();

        JsonObject document = new JsonObject().put("id",token);
        mongoClient.removeDocuments(dbName, document, res ->{
            if (res.succeeded()) {
                promise.complete("Deleting complete");
            } else {
                log.info("Deleting failed: " + res.cause());
                promise.fail(res.cause());
            }
        });

        return promise.future();
    }


    public Future<String> deleteEntryByFileID(String fileID){
        Promise<String> promise = Promise.promise();

        JsonObject document = new JsonObject().put("id",fileID);
        mongoClient.removeDocuments(dbName,document, res -> {
            if (res.succeeded()) {
                res.result();
                promise.complete(res.result().toString());
            } else {
                promise.fail(res.cause());
            }
        });

        return promise.future();
    }


    public void showAllForOwner(String ownerID, Handler<List> handler){
        JsonObject queryDocument = new JsonObject().put("owner",ownerID);
        mongoClient.find(dbName,queryDocument, res ->{

            if (res.succeeded()) {
                List<JsonObject> result = res.result();
                List<String> returnList= new ArrayList<>();
                for (JsonObject entries : result) {
                    entries.remove("_id");
                    entries.remove("BinaryData");
                }

                handler.handle(res.result());
            } else {
                handler.handle(null);
            }
        });

    }

    public void getFile(String fileID, Handler<File> aHandler){
        JsonObject queryDocument = new JsonObject()
                .put("id",fileID);
        AtomicReference<String> tmpFileName= new AtomicReference<>("");
        mongoClient.find(dbName,queryDocument, res ->{

            if (res.succeeded() && res.result().size()>0) {
                JsonObject returnDocument = res.result().get(0);
                tmpFileName.set("/tmp/" + returnDocument.getString("fileName"));
                try {
                    FileUtils.writeByteArrayToFile(new File(String.valueOf(tmpFileName)), returnDocument.getBinary("binaryData"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                returnDocument.getBinary("binaryData");
                aHandler.handle(new File(String.valueOf(tmpFileName)));
            } else {
                aHandler.handle(null);
            }
        });

    }


}
