package com.decagon.fitnessoapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductDetailsRequest {

    private String productName;
}
