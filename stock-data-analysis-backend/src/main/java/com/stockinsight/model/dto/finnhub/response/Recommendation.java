package com.stockinsight.model.dto.finnhub.response;

import lombok.Data;

import java.util.Date;

@Data
public class Recommendation {
    private String symbol;
    private Date period;
    private int strongBuy;
    private int buy;
    private int hold;
    private int sell;
    private int strongSell;
}
