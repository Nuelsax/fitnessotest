package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.Order;
import com.decagon.fitnessoapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('PREMIUM')"+ "|| hasRole('ADMIN')")
    @GetMapping("/viewOrder")
    public ResponseEntity<OrderResponse> viewOrder(Authentication authentication){
        return orderService.getOrder(authentication);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrders")
    public ResponseEntity<List<Order>> viewAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrdersBy/{status}")
    public ResponseEntity<List<Order>> viewOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }
}
