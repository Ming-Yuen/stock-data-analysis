package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class MarketNews extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String publishedAt;

    @Column(nullable = false, updatable = false)
    private String symbol;

    private String title;

    private String source;

    private String url;

    private String tags;

    private String summary;
}