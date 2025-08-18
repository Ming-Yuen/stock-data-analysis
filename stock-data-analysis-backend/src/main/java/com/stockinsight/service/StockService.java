package com.stockinsight.service;

import com.stockinsight.repository.StockRepository;
import com.stockinsight.repository.StockQuoteRepository;
import com.stockinsight.repository.StockRecommendationRepository;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.model.entity.StockQuote;
import com.stockinsight.model.entity.StockRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockQuoteRepository stockQuoteRepository;
    private final StockRecommendationRepository stockRecommendationRepository;

    public void update(List<Stock> stocks) {
    }

    public void stockUpdate(ArrayList<Stock> stocks) {
        stockRepository.saveAll(stocks);
    }

    public void stockQuoteUpdate(List<StockQuote> stockQuotes) {
        stockQuoteRepository.saveAll(stockQuotes);
    }

    public void stockRecommendationsUpdate(ArrayList<StockRecommendation> stockRecommendations) {
        stockRecommendationRepository.saveAll(stockRecommendations);
    }

    public boolean getStockQuoteExists(String symbol, LocalDateTime localDateTime) {
        return stockQuoteRepository.existsBySymbolAndQuoteDate(symbol, localDateTime);
    }
}
