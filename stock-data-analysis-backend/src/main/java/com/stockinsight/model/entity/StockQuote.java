package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
public class StockQuote extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String symbol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime quoteDate;

    private BigDecimal openPrice;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal closePrice;

    private BigDecimal volume;

    private BigDecimal marketCap;

    private String dataSource;
}