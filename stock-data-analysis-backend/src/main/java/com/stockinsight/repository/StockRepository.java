package com.stockinsight.repository;

import com.stockinsight.model.entity.Stock;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>  {

    Stock findByExchangeCodeAndSymbol(String exchangeCode, String symbol);

    List<Stock> findByExchangeCode(String exchangeCode);

    boolean existsBySymbol(@NotBlank String symbol);
}