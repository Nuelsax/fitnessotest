package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.product.SHIPPING_METHOD;
import com.decagon.fitnessoapp.model.product.ShoppingItem;
import com.decagon.fitnessoapp.model.user.Address;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private Long Id;
    private List<ShoppingItem> shoppingItems;
    private Double totalPrice;
    private Address shippingAddress;
    private String orderStatus;
    private SHIPPING_METHOD shippingMethod;
}
