package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        name = "uk_stock_quote",
        columnNames = {"exchange_code", "symbol", "quote_date"}
    )
)
public class StockQuote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_quote_seq")
    @SequenceGenerator(name = "stock_quote_seq", sequenceName = "stock_quote_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "symbol", nullable = false, length = 30, updatable = false)
    private String symbol;

    @Column(name = "quote_date", nullable = false, updatable = false)
    private LocalDate quoteDate;

    @Column(name = "open_price", precision = 15, scale = 4)
    private BigDecimal openPrice;

    @Column(name = "high_price", precision = 15, scale = 4)
    private BigDecimal highPrice;

    @Column(name = "low_price", precision = 15, scale = 4)
    private BigDecimal lowPrice;

    @Column(name = "close_price", nullable = false, precision = 15, scale = 4)
    private BigDecimal closePrice;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "market_cap", precision = 18, scale = 2)
    private BigDecimal marketCap;

    @Column(name = "data_source", length = 50)
    private String dataSource;
}