package com.tilacyn.exchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/name")
    public String hello() {
        return "Hello, ";
    }
}
