package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface OrderService {
    ResponseEntity<OrderResponse> getOrder(Authentication authentication);
}
