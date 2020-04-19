package com.tilacyn.trader.office.service;


import com.tilacyn.trader.office.rest.EmulatorRestService;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.*;


public class TradeOfficeServiceTest {
    private TradeOfficeService service = new TradeOfficeService(new EmulatorRestService(8081));


    @ClassRule
    public static GenericContainer simpleWebServer
            = new FixedHostPortGenericContainer("emulator:1.0-SNAPSHOT")
            .withFixedExposedPort(8081, 8080);


    @Test
    public void testQuote() {
        assertTrue(service.getQuote("AAPL") < 270);
        assertTrue(service.getQuote("AAPL") > 230);
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
    }
}
