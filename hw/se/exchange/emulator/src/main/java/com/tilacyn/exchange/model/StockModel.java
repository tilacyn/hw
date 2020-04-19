package com.tilacyn.exchange.model;

import com.tilacyn.exchange.to.AddCompanyTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@Builder
public class StockModel {
    private String companyName;
    private String symbol;
    private Long volume;
    private Double price;

    public static StockModel from(JSONObject singleStockData) {
        return new StockModelBuilder()
                .companyName(singleStockData.getString("name"))
                .price(singleStockData.getDouble("price"))
                .volume(singleStockData.getLong("volume"))
                .symbol(singleStockData.getString("symbol"))
                .build();
    }

    public static StockModel from(AddCompanyTO addCompanyTO) {
        return new StockModelBuilder()
                .companyName(addCompanyTO.getCompanyName())
                .symbol(addCompanyTO.getSymbol())
                .volume(addCompanyTO.getInitialVolume())
                .price(addCompanyTO.getInitialPrice())
                .build();
    }
}
