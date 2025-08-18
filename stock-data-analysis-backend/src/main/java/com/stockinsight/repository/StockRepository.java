package com.stockinsight.repository;

import com.stockinsight.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}