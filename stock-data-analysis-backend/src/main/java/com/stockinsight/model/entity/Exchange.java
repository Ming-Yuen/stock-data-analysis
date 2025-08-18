package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Exchange extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String exchangeCode;

    private String name;

    private String mic;

    private String timezone;

    private LocalDateTime preMarketStartTime;

    private LocalDateTime preMarketEndTime;

    private LocalDateTime regularMarketStartTime;

    private LocalDateTime regularMarketEndTime;

    private LocalDateTime postMarketStartTime;

    private LocalDateTime postMarketEndTime;

    private String closeDate;

    private String country;

    private String reference;
}