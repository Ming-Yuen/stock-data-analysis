package com.stockinsight.service;

import com.stockinsight.converter.StockConverter;
import com.stockinsight.repository.StockRepository;
import com.stockinsight.repository.StockQuoteRepository;
import com.stockinsight.repository.StockRecommendationRepository;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.model.entity.StockQuote;
import com.stockinsight.model.entity.StockRecommendation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockQuoteRepository stockQuoteRepository;
    private final StockRecommendationRepository stockRecommendationRepository;
    private final StockConverter stockConverter;
    @Transactional
    public void stockUpdate(ArrayList<Stock> stocks) {
        Map<String, List<Stock>> exchangeMap = stocks.stream().collect(Collectors.groupingBy(Stock::getExchangeCode));
        for(String exchangeCode : exchangeMap.keySet()){
            Map<String, Stock> persistedExchangeMap = stockRepository.findByExchangeCode(exchangeCode)
                    .stream().collect(Collectors.toMap(Stock::getSymbol, Function.identity()));

            ArrayList<Stock> persistedStockList = new ArrayList<>();
            for(Stock stock : exchangeMap.get(exchangeCode)){
                Stock persistedStock = persistedExchangeMap.get(stock.getSymbol());
                persistedStockList.add(stockConverter.mergeToPersistedEntity(stock, persistedStock));
            }
            stockRepository.saveAll(persistedStockList);
        }
    }

    public void stockQuoteUpdate(List<StockQuote> stockQuotes) {
        stockQuoteRepository.saveAll(stockQuotes);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stockQuoteSave(StockQuote stockQuote) {
        stockQuoteRepository.save(stockQuote);
    }

    public void stockRecommendationsUpdate(ArrayList<StockRecommendation> stockRecommendations) {
        stockRecommendationRepository.saveAll(stockRecommendations);
    }

    public boolean getStockQuoteExists(String symbol, LocalDateTime localDateTime) {
        return stockQuoteRepository.existsBySymbolAndQuoteDate(symbol, localDateTime);
    }

    public boolean existsBySymbol(@NotBlank String symbol) {
        return stockRepository.existsBySymbol(symbol);
    }

    public List<Stock> getExchangeStock(String exchangeCode){
        return stockRepository.findByExchangeCode(exchangeCode);
    }

    public List<Stock> findByAll() {
        return stockRepository.findAll();
    }
}
