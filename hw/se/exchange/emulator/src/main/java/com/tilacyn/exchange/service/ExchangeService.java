package com.tilacyn.exchange.service;

import com.tilacyn.exchange.model.ExchangeModel;
import com.tilacyn.exchange.model.StockModel;
import com.tilacyn.exchange.to.AddCompanyTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Service
public class ExchangeService {

    private ExchangeModel exchangeModel;

    public ExchangeService(ExchangeModel exchangeModel) {
        this.exchangeModel = exchangeModel;
        loadStocks();
    }


    private void loadStocks() {
        String stockDataString = readFileToString("/stock.json");
        System.out.println(stockDataString);
        JSONArray jsonArray = new JSONArray(stockDataString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject singleStockData = jsonArray.getJSONObject(i);
            exchangeModel.addStock(StockModel.from(singleStockData));
        }
    }

    public Double getQuote(String symbol) {
        return exchangeModel.getStock(symbol).getPrice();
    }

    public Long getVolume(String symbol) {
        return exchangeModel.getStock(symbol).getVolume();
    }

    public void addCompany(AddCompanyTO addCompanyTO) {
        exchangeModel.addStock(StockModel.from(addCompanyTO));
    }

    private String readFileToString(String path) {
        try {
            InputStream in = getClass().getResourceAsStream(path);
            return StreamUtils.copyToString(in, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public double buy(String symbol, int qty) {
        return getQuote(symbol) * qty;
    }

    public double sell(String symbol, int qty) {
        return getQuote(symbol) * qty;
    }
}
