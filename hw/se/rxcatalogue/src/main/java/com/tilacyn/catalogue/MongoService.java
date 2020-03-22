package com.tilacyn.catalogue;

import com.mongodb.rx.client.*;
import org.bson.Document;
import org.json.JSONObject;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MongoDriver {
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    private static final String DB_NAME = "rxcatalogue";
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String GOODS_COLLECTION_NAME = "goods";

    private MongoDatabase db;

    {
        db = mongoClient.getDatabase(DB_NAME);

        createCollection(USERS_COLLECTION_NAME);
        createCollection(GOODS_COLLECTION_NAME);

        db.getCollection(GOODS_COLLECTION_NAME)
                .count()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .filter(count -> count == 0)
                .subscribe(count -> {
                    try {
                        fillGoodsCollection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Observable<Success> addUser(String id, String currency) {
        return db.getCollection(USERS_COLLECTION_NAME).insertOne(new Document(id, currency));
    }

    public Observable<Document> getGoods() {
        return db.getCollection(GOODS_COLLECTION_NAME).find().toObservable();
    }

    public Observable<String> getUserCurrency(String id) {
        return db.getCollection(USERS_COLLECTION_NAME).find().toObservable()
                .filter(document -> document.containsKey(id))
                .take(1)
                .map(document -> String.valueOf(document.get(id)));
    }

    private void createCollection(String collectionName) {
        String noCollectionCreatedMessages = String.format("no_%s_collection", collectionName);
        db.listCollectionNames()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .filter(name -> name.equals(collectionName))
                .take(1)
                .defaultIfEmpty(noCollectionCreatedMessages)
                .filter(name -> name.equals(noCollectionCreatedMessages))
                .subscribe(name -> db.createCollection(collectionName).subscribe());
    }

    private void fillGoodsCollection() throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get("src/main/resources/goods.json")));
        JSONObject object = new JSONObject(jsonString);
        object.toMap().forEach((key, value) -> db.getCollection(GOODS_COLLECTION_NAME).insertOne(new Document(key, value)).subscribe());
    }

    public Observable<Success> addGood(String name, double price) {
        return db.getCollection(GOODS_COLLECTION_NAME).insertOne(new Document(name, price));
    }
}
