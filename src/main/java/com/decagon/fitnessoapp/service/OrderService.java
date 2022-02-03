package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {
    ResponseEntity<OrderResponse> getOrder(Authentication authentication);

    ResponseEntity<List<Order>> getAllOrders();

    ResponseEntity<List<Order>> getOrdersByStatus(String status);
}
