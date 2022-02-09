package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItemResponse {
    private Map<String, Integer> intangibleProduct = new HashMap<String, Integer>();
    private Map<String, Integer> tangibleProduct = new HashMap<String, Integer>();
    private Long id;

}
