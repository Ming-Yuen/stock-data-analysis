package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Stock extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String exchangeCode;

    @Column(nullable = false, updatable = false)
    private String symbol;

    private String description;

    private String currency;

    private String mic;

    private String type;

    private String figi;

    private String shareClassFigi;

    private String isin;

    private String industry;

    private LocalDateTime listingDate;

    private String website;

    private String country;

    private Long marketCap;

    private String logoUrl;
}