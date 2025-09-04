package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
            name = "uk_exchange",
            columnNames = {"exchangeCode"}
    )
)
@DynamicUpdate
public class Exchange extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_seq")
    @SequenceGenerator(name = "exchange_seq", sequenceName = "exchange_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "exchange_code", nullable = false, length = 10, updatable = false)
    private String exchangeCode;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "mic", length = 200)
    private String mic;

    @Column(name = "timezone", length = 30)
    private String timezone;

    @Column(name = "pre_market_start_time")
    private LocalDateTime preMarketStartTime;

    @Column(name = "pre_market_end_time")
    private LocalDateTime preMarketEndTime;

    @Column(name = "regular_market_start_time")
    private LocalDateTime regularMarketStartTime;

    @Column(name = "regular_market_end_time")
    private LocalDateTime regularMarketEndTime;

    @Column(name = "post_market_start_time")
    private LocalDateTime postMarketStartTime;

    @Column(name = "post_market_end_time")
    private LocalDateTime postMarketEndTime;

    @Column(name = "close_date", length = 30)
    private String closeDate;

    @Column(name = "country", length = 30)
    private String country;

    @Column(name = "reference", length = 300)
    private String reference;
}