package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.model.product.Order;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {
    ResponseEntity<OrderResponse> getOrder(Authentication authentication);

    Page<OrderResponse> getAllOrders(int pageNo);

    Page<OrderResponse> getOrdersByStatus(ORDER_STATUS status, int pageNo);

   // ResponseEntity<List<Order>> getAllOrders();

    ResponseEntity<List<Order>> getOrdersByStatus(String status);
}
