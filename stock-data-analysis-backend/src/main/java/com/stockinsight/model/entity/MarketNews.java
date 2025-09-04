package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MarketNews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "market_news_seq")
    @SequenceGenerator(name = "market_news_seq", sequenceName = "market_news_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "published_at", nullable = false, length = 10, updatable = false)
    private String publishedAt;

    @Column(name = "symbol", length = 30, updatable = false)
    private String symbol;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "source", length = 255)
    private String source;

    @Column(name = "url", length = 2000)
    private String url;

    @Column(name = "tags", length = 255)
    private String tags;

    @Column(name = "summary", length = 2000)
    private String summary;
}