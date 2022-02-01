package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('PREMIUM')"+ "|| hasRole('ADMIN')")
    @GetMapping("/viewOrder")
    private ResponseEntity<?> viewOrder(Authentication authentication){
        return orderService.getOrder(authentication);
    }
}
