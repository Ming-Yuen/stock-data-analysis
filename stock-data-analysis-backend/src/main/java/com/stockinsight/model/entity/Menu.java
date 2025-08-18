package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Menu extends BaseEntity {
    @Column(nullable = false, updatable = false)
    private String menuId;

    private String parentId;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String icon;

    @Column(nullable = false, updatable = false)
    private String path;
}