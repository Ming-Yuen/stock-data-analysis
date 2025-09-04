package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
public class StockRecommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_recommendation_seq")
    @SequenceGenerator(name = "stock_recommendation_seq", sequenceName = "stock_recommendation_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "symbol", nullable = false, length = 30, updatable = false)
    private String symbol;

    @Column(name = "period", nullable = false, updatable = false)
    private LocalDate period;

    @Column(name = "strong_buy", nullable = false)
    private Integer strongBuy;

    @Column(name = "buy", nullable = false)
    private Integer buy;

    @Column(name = "hold", nullable = false)
    private Integer hold;

    @Column(name = "sell", nullable = false)
    private Integer sell;

    @Column(name = "strong_sell", nullable = false)
    private Integer strongSell;

    @Column(name = "total_recommendations")
    private Integer totalRecommendations;
}