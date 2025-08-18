package com.stockinsight.model.dto.finnhub.response;

import lombok.Data;

import java.util.Date;

@Data
public class MarketNews {
    private String category;
    private Long datetime;
    private String headline;
    private Long id;
    private String image;
    private String related;
    private String source;
    private String summary;
    private String url;
}
