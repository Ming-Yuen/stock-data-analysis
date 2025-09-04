package com.stockinsight.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class StockEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_event_seq")
    @SequenceGenerator(name = "stock_event_seq", sequenceName = "stock_event_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "symbol", nullable = false, length = 30)
    private String symbol;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "event_subtype", length = 100)
    private String eventSubtype;

    @Column(name = "event_title", nullable = false, length = 500)
    private String eventTitle;

    @Lob
    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "impact_level", length = 20)
    private String impactLevel;

    @Column(name = "source", length = 255)
    private String source;

    @Lob
    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "regulatory_body", length = 100)
    private String regulatoryBody;
}
