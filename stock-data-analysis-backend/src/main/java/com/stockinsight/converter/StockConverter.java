package com.stockinsight.converter;

import com.stockinsight.model.dto.finnhub.response.Quote;
import com.stockinsight.model.dto.finnhub.response.Recommendation;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.model.entity.StockQuote;
import com.ykm.common.common_lib.converter.CommonTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StockConverter extends CommonTypeMapper {

    default ArrayList<Stock> toStocks(String exchangeCode, List<Symbol> symbols) {
        if ( exchangeCode == null && symbols == null ) {
            return null;
        }

        ArrayList<Stock> arrayList = new ArrayList<Stock>();
        for (Symbol symbol : symbols) {
            arrayList.add(toStock(exchangeCode, symbol));
        }
        return arrayList;
    }

    @Mapping(target = "symbol", source = "symbol.symbol")
    @Mapping(target = "exchangeCode", source = "exchangeCode")
    @Mapping(target = "description", source = "symbol.displaySymbol")
    @Mapping(target = "type", defaultValue = "Unknown")
    Stock toStock(String exchangeCode, Symbol symbol);

    @Mapping(target = "openPrice", source = "quote.o")
    @Mapping(target = "highPrice", source = "quote.h")
    @Mapping(target = "lowPrice", source = "quote.l")
    @Mapping(target = "closePrice", source = "quote.c")
    @Mapping(target = "quoteDate", source = "localDateTime")
    @Mapping(target = "volume", ignore = true)
    @Mapping(target = "marketCap", ignore = true)
    @Mapping(target = "dataSource", constant = "finnhub")
    StockQuote toStockQuote(LocalDateTime localDateTime, String symbol, Quote quote);

    StockQuote toStockRecommendations(Recommendation recommendation);
}

