package com.tilacyn.exchange.to;


import lombok.Data;

@Data
public class AddCompanyTO {
    private String companyName;
    private String symbol;
    private Long initialVolume;
    private Double initialPrice;
}
