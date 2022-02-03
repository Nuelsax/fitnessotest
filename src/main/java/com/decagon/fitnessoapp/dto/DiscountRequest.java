package com.decagon.fitnessoapp.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class DiscountRequest {
    private String couponCode;

    private float discountPercent;
}
