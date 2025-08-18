package com.stockinsight.batch.writer;

import com.stockinsight.converter.MarketNewsConverter;
import com.stockinsight.converter.StockConverter;
import com.stockinsight.model.dto.finnhub.response.Quote;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import com.stockinsight.model.entity.Exchange;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.model.entity.StockQuote;
import com.stockinsight.service.ExchangeService;
import com.stockinsight.service.FinnhubService;
import com.stockinsight.service.MarketNewsService;
import com.stockinsight.service.StockService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeItemWriter implements ItemWriter<Exchange> {

    private final ExchangeService exchangeService;
    private final FinnhubService finnhubService;

    private final StockService stockService;
    private final StockConverter stockConverter;

    private final MarketNewsService marketNewsService;
    private final MarketNewsConverter marketNewsConverter;

    @Override
    public void write(Chunk<? extends Exchange> chunk) {
        List<Exchange> exchangeList = new ArrayList<>(chunk.getItems());
        Exchange exchange = exchangeList.stream().filter(x->"US".equals(x.getExchangeCode())).findAny().get();
        exchangeService.update(exchange);

        List<Symbol> symbols = finnhubService.getStockSymbols(exchange.getExchangeCode());
        ArrayList<Stock> stocks = stockConverter.toStocks(exchange.getExchangeCode(), symbols);
        stockService.stockUpdate(stocks);


//        List<MarketNews> marketNews = finnhubService.getMarketNews(exchange.getExchangeCode());
//        marketNewsService.insert(marketNewsConverter.toMarketNews(marketNews));

        List<StockQuote> stockQuotes = new ArrayList<>();
        int batchSize = 1;
        LocalDateTime localDateTime = exchangeService.getPreviousTradingDay(LocalDate.now()).atStartOfDay();

//        for (int i = 0; i < stocks.size(); i++) {
//            Stock stock = stocks.get(i);
//
//            if(StringUtils.isBlank(stock.getType())){
//                continue;
//            }
//
//            if(!stockService.getStockQuoteExists(stock.getSymbol(), localDateTime)) {
//                log.info(stock.toString());
//                Quote quote = finnhubService.getStockQuote(stock.getSymbol());
//                StockQuote stockQuote = stockConverter.toStockQuote(localDateTime, stock.getSymbol(), quote);
//
//                stockQuotes.add(stockQuote);
//
//                if (stockQuotes.size() == batchSize || i == stocks.size() - 1) {
//                    stockService.stockQuoteUpdate(stockQuotes);
//                    stockQuotes.clear();
//                }
//            }
//        }
//
//        ArrayList<StockRecommendation> stockRecommendations = new ArrayList<>();
//        for(Stock stock : stocks){
//            Recommendation recommendation = finnhubService.getRecommendation(stock.getSymbol());
//            stockQuotes.add(stockConverter.toStockRecommendations(recommendation));
//        }
//        stockService.stockRecommendationsUpdate(stockRecommendations);
    }
}

