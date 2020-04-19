package com.tilacyn.trader.office.service;


import com.tilacyn.trader.office.rest.EmulatorRestService;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.*;


public class TradeOfficeServiceTest {


    @ClassRule
    public static GenericContainer simpleWebServer
//            = new FixedHostPortGenericContainer("emulator:1.0-SNAPSHOT")
            = new GenericContainer("emulator:1.0-SNAPSHOT")
            .withExposedPorts(8080);


    @Test
    public void testQuote() {
        TradeOfficeService service = new TradeOfficeService(
                new EmulatorRestService(simpleWebServer.getMappedPort(8080), simpleWebServer.getContainerIpAddress()));
        assertTrue(service.getQuote("AAPL") < 270);
        assertTrue(service.getQuote("AAPL") > 230);
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
    }
}
