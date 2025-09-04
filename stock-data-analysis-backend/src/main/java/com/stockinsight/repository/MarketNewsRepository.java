package com.stockinsight.repository;

import com.stockinsight.model.entity.MarketNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketNewsRepository extends JpaRepository<MarketNews, Long>{
}