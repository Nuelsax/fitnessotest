package com.decagon.fitnessoapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface OrderService {
    ResponseEntity<?> getOrder(Authentication authentication);
}
