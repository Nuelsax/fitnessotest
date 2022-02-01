package com.decagon.fitnessoapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductDetailsResponse {

    private Long id;

    private String category;

    private String StockKeepingUnit;

    private String productName;

    private BigDecimal price;

    private String description;

    private String image;

    private Integer durationInHoursPerDay;

    private Integer durationInDays;

    private Integer quantity;
}
