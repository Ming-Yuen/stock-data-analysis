package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "batch_job_configs")
public class BatchJonConfig extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private String name;

    private String configJson;

    private Long isActive;
}