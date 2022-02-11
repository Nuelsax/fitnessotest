package com.decagon.fitnessoapp.dto;

import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.model.product.SHIPPING_METHOD;
import com.decagon.fitnessoapp.model.product.TRANSACTION_STATUS;
import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.PaymentCard;
import com.decagon.fitnessoapp.model.user.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String firstName;
    private String lastName;
    private String email;
    private List<Cart> cartList;
    private BigDecimal totalPrice;
    private Address shippingAddress;
    private Address billingAddress;
    private Timestamp orderDate;
    private String couponCode;
    private String referenceNumber;
    private SHIPPING_METHOD shippingMethod;
    private TRANSACTION_STATUS transactionStatus;
    private ORDER_STATUS orderStatus;
}
