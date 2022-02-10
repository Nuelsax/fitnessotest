package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponse getOrder(Authentication authentication);

    Page<OrderResponse> getAllOrders(int pageNo);

    Page<OrderResponse> getOrdersByStatus(ORDER_STATUS status, int pageNo);
}
