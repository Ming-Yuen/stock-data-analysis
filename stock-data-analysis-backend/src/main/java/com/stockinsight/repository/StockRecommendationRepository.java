package com.stockinsight.repository;

import com.stockinsight.model.entity.StockRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRecommendationRepository extends JpaRepository<StockRecommendation, Long> {
}