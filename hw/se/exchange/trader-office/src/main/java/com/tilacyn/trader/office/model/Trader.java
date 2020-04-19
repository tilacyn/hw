package com.tilacyn.trader.office.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trader {
    private int id;
    private String fullName;
    private Double balance;

}
