package com.tilacyn.trader.office.service;


import com.tilacyn.trader.office.model.Trader;
import com.tilacyn.trader.office.rest.EmulatorRestService;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.*;


public class TradeOfficeServiceTest {


    @ClassRule
    public static GenericContainer simpleWebServer
            = new GenericContainer("emulator:1.0-SNAPSHOT")
            .withExposedPorts(8080);

    @Test
    public void testQuote() {
        TradeOfficeService service = initService();
        assertTrue(service.getQuote("AAPL") < 270);
        assertTrue(service.getQuote("AAPL") > 230);
    }

    @Test
    public void testBuy() {
        TradeOfficeService service = initService();
        Trader trader1 = getTrader();
        service.addTrader(trader1);
        service.buy(trader1.getId(), "AAPL", 2);
        assertTrue(trader1.getBalance() < 700);
        assertTrue(trader1.getBalance() > 300);
    }

    @Test
    public void testSell() {
        TradeOfficeService service = initService();
        Trader trader1 = getTrader();
        service.addTrader(trader1);
        service.sell(trader1.getId(), "AAPL", 2);
        assertTrue(trader1.getBalance() > 1300);
        assertTrue(trader1.getBalance() < 1700);
    }

    private TradeOfficeService initService() {
        TradeOfficeService service = new TradeOfficeService(
                new EmulatorRestService(simpleWebServer.getMappedPort(8080), simpleWebServer.getContainerIpAddress()));
        return service;
    }

    private Trader getTrader() {
        return Trader.builder()
                .balance(1000.0)
                .fullName("John Doe")
                .id(1)
                .build();
    }
}
