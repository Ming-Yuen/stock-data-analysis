package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
public class StockRecommendation extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String symbol;

    private LocalDateTime period;

    private BigDecimal strongBuy;

    private BigDecimal buy;

    private BigDecimal hold;

    private BigDecimal sell;

    private BigDecimal strongSell;

    private BigDecimal totalRecommendations;
}