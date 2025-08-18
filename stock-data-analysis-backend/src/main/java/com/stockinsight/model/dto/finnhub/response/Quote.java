package com.stockinsight.model.dto.finnhub.response;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Quote {
    /** Current price */
    private BigDecimal c;

    /** Change */
    private BigDecimal d;

    /** Percent change */
    private BigDecimal dp;

    /** High price of the day */
    private BigDecimal h;

    /** Low price of the day */
    private BigDecimal l;

    /** Open price of the day */
    private BigDecimal o;

    /** Previous close price */
    private BigDecimal pc;
    private Long t;
}
