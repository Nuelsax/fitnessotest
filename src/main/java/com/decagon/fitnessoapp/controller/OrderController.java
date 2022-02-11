package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasAnyRole('ROLE_PREMIUM', 'ROLE_ADMIN')")
    @GetMapping("/viewOrder")
    public ResponseEntity<OrderResponse> viewOrder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body(orderService.getOrder(authentication));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrders/{pageNo}")
    public ResponseEntity<List<OrderResponse>> viewAllOrders(@PathVariable(value = "pageNo") int pageNo) {
        final List<OrderResponse> allOrders = orderService.getAllOrders(pageNo);
        return ResponseEntity.ok(allOrders);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrdersBy/{status}/{pageNo}")
    public ResponseEntity<Page<OrderResponse>> viewOrdersByStatus(@PathVariable(value = "status") ORDER_STATUS status, @PathVariable(value = "pageNo") int pageNo) {
        final Page<OrderResponse> ordersByStatus = orderService.getOrdersByStatus(status, pageNo);
        return ResponseEntity.ok(ordersByStatus);
    }
}
