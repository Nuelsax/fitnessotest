package com.decagon.fitnessoapp.model.product;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "stock_keeping_unit",nullable = false)
    private String stockKeepingUnit;

    @NotNull
    @Column(nullable = false)
    private String category;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    private String image;
}
