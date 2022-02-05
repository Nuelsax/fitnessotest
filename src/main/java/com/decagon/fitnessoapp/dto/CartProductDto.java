package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.user.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    private String category;
    private String productName;
    private BigDecimal price;
    private Person person;
    private String description;
    private String stockKeepingUnit;
    private String ProductType;
    private String image;
    private Integer durationInHoursPerDay;
    private Integer durationInDays;
    private Integer quantity;
}
