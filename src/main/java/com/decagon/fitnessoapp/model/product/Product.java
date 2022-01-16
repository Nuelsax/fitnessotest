package com.decagon.fitnessoapp.model.product;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_keeping_unit")
    private String stockKeepingUnit;

    private String category;

    @Column(name = "product_name")
    private String productName;

    private BigDecimal price;

    private String description;

    private String image;
}
