package com.tilacyn.exchange.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExchangeModel {
    private Map<String, StockModel> stocks = new HashMap<>();


    public void addStock(StockModel stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    public StockModel getStock(String symbol) {
        return stocks.get(symbol);
    }
}
