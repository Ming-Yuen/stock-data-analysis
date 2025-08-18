package com.stockinsight.repository;

import com.stockinsight.model.entity.StockQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StockQuoteRepository extends JpaRepository<StockQuote, Long> {
    boolean existsBySymbolAndQuoteDate(String symbol, LocalDateTime localDateTime);
}