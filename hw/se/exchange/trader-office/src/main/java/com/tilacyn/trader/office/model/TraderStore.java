package com.tilacyn.trader.office.model;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class TraderStore {
    private Map<Integer, Trader> traders = new HashMap<>();

    public void deposit(int id, double amount) {
        Trader trader = traders.get(id);
        double balance = trader.getBalance();
        double newBalance = balance + amount;
        trader.setBalance(newBalance);
    }

    public void withdraw(int id, double amount) {
        Trader trader = traders.get(id);
        double balance = trader.getBalance();
        double newBalance = balance - amount;
        trader.setBalance(newBalance);
    }

    public void add(Trader trader) {
        traders.put(trader.getId(), trader);
    }
}
