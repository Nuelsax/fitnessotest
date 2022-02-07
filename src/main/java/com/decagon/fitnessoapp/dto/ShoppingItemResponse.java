package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItemResponse {
    private IntangibleProduct intangibleProducts;

    private TangibleProduct tangibleProducts;

    private Integer quantity;
}
