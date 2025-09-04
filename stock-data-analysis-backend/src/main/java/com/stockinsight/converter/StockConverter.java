package com.stockinsight.converter;

import com.stockinsight.model.dto.finnhub.response.Quote;
import com.stockinsight.model.dto.finnhub.response.Recommendation;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.model.entity.StockQuote;
import com.stockinsight.model.entity.StockRecommendation;
import com.ykm.common.common_lib.converter.CommonTypeConverter;
import com.ykm.orm.EntityConverter;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StockConverter extends CommonTypeConverter, EntityConverter<Stock> {

    ArrayList<Stock> toStocks(List<Symbol> symbols);

    @Mapping(target = "symbol", source = "symbol")
    @Mapping(target = "description", source = "displaySymbol")
    @Mapping(target = "type", defaultValue = "Unknown")
    @Mapping(target = "exchangeCode", constant = "US")
    Stock toStock(Symbol symbol);

    @Mapping(target = "openPrice", source = "quote.o")
    @Mapping(target = "highPrice", source = "quote.h")
    @Mapping(target = "lowPrice", source = "quote.l")
    @Mapping(target = "closePrice", source = "quote.c")
    @Mapping(target = "quoteDate", source = "localDateTime")
    @Mapping(target = "volume", ignore = true)
    @Mapping(target = "marketCap", ignore = true)
    @Mapping(target = "dataSource", constant = "finnhub")
    StockQuote toStockQuote(LocalDateTime localDateTime, String symbol, Quote quote);

    StockRecommendation toStockRecommendations(Recommendation recommendation);
}

