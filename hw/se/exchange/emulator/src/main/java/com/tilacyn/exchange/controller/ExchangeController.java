package com.tilacyn.exchange.controller;

import com.tilacyn.exchange.service.ExchangeService;
import com.tilacyn.exchange.to.AddCompanyTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ExchangeController {

    private ExchangeService service;

    @PutMapping("/company/add")
    public String addCompany(@RequestBody AddCompanyTO addCompanyTO) {
        service.addCompany(addCompanyTO);
        return "Successfully added";
    }

    @GetMapping("/quote")
    public String getQuote(@RequestParam(name = "symbol") String symbol) {
        return String.valueOf(service.getQuote(symbol));
    }

    @GetMapping("/volume")
    public String getVolume(@RequestParam(name = "symbol") String symbol) {
        return String.valueOf(service.getVolume(symbol));
    }

    @PutMapping("/buy")
    public double buy(@RequestParam(name = "symbol") String symbol, @RequestParam(name = "qty") int qty) {
        return service.buy(symbol, qty);
    }


}
