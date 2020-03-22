package com.tilacyn.catalogue;

import com.mongodb.rx.client.Success;
import org.bson.Document;
import rx.Observable;

import java.util.stream.Collectors;

public class CatalogueManager {

    private MongoService mongoService = new MongoService();


    public Observable<Success> addUser(String id, String currency) {
        return mongoService.addUser(id, currency);
    }


    public Observable<String> observeGoods(String id) {
        return mongoService.getUserCurrency(id).flatMap(
                currency -> mongoService.getGoods()
                        .map(good -> goodToString(currency, good))
        ).defaultIfEmpty("No goods in the store");
    }

    private String goodToString(String currencyString, Document good) {
        Currency currency = Currency.fromString(currencyString);
        return good.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("_id"))
                .map(entry ->
                        String.format("%s : %f %s\n", entry.getKey(),
                                Currency.convertTo(currency,
                                        Double.parseDouble(entry.getValue().toString())
                                ),
                                currency.toString()))
                .collect(Collectors.joining());
    }

    public Observable<Success> addGood(String name, double price) {
        return mongoService.addGood(name, price);
    }
}
