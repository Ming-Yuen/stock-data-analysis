package com.stockinsight.model.entity;

import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
            name = "uk_menu",
            columnNames = {"menu_id"}
    )
)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq")
    @SequenceGenerator(name = "menu_seq", sequenceName = "menu_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "menu_id", nullable = false, updatable = false)
    private Integer menuId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "icon", nullable = false, length = 16)
    private String icon;

    @Column(name = "path", nullable = false, length = 255)
    private String path;
}