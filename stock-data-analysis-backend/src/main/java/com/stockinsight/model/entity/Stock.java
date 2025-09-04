package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
@Data
@Entity
//@Audited
@Table(
    uniqueConstraints = @UniqueConstraint(
        name = "uk_stock",
        columnNames = {"exchange_code", "symbol"}
    )
)
@DynamicUpdate
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    @SequenceGenerator(name = "stock_seq", sequenceName = "stock_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "exchange_code", nullable = false, length = 10, updatable = false)
    private String exchangeCode;

    @Column(name = "symbol", nullable = false, length = 30, updatable = false)
    private String symbol;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "currency", length = 16)
    private String currency;

    @Column(name = "mic", length = 16)
    private String mic;

    @Column(name = "type", length = 32)
    private String type;

    @Column(name = "figi", length = 32)
    private String figi;

    @Column(name = "share_class_figi", length = 32)
    private String shareClassFigi;

    @Column(name = "isin", length = 32)
    private String isin;

    @Column(name = "industry", length = 64)
    private String industry;

    @Column(name = "listing_date")
    private LocalDateTime listingDate;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "country", length = 64)
    private String country;

    @Column(name = "market_cap")
    private Long marketCap;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;
}