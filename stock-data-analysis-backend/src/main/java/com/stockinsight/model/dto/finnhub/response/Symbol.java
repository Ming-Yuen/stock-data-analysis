package com.stockinsight.model.dto.finnhub.response;

import lombok.Data;

@Data
public class Symbol {
    private String description;

    private String displaySymbol;

    private String symbol;

    private String type;

    private String mic;

    private String figi;

    private String shareClassFIGI;

    private String currency;
}
