package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.product.SHIPPING_METHOD;
import lombok.Data;

@Data
public class CheckOutRequest {
    private String email;
    private AddressRequest shippingAddress;
    private AddressRequest billingAddress;
    private PaymentRequest paymentRequest;
    private DiscountRequest discountRequest;
    private String paymentMethod;
    private SHIPPING_METHOD shippingMethod;
}
