package com.tilacyn.trader.office.service;

import com.tilacyn.trader.office.model.TraderStore;
import com.tilacyn.trader.office.rest.EmulatorRestService;

public class TradeOfficeService {
    private EmulatorRestService emulatorRestService;
    TraderStore traderStore = new TraderStore();

    public TradeOfficeService(EmulatorRestService emulatorRestService) {
        this.emulatorRestService = emulatorRestService;
    }

    public Double getQuote(String symbol) {
        return emulatorRestService.getQuote(symbol);
    }

    public void buy(int id, String symbol, int qty) {
        double amount = emulatorRestService.buy(symbol, qty);
        traderStore.withdraw(id, amount);
    }

    public void sell(int id, String symbol, int qty) {
        double amount = emulatorRestService.sell(symbol, qty);
        traderStore.deposit(id, amount);
    }
}
