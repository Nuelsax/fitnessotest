package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.OrderResponse;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

   /* @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrders/{pageNo}")
    public ResponseEntity<Page<OrderResponse>> viewAllOrders(@PathVariable(value = "pageNo") int pageNo) {
        final Page<OrderResponse> allOrders = orderService.getAllOrders(pageNo);
        return ResponseEntity.ok(allOrders);
    }*/

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrders/{pageNo}")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(defaultValue = "0") Integer pageNo) {
        List<OrderResponse> orderResponses = orderService.getAlOrders(pageNo);
        return new ResponseEntity<>(
                orderResponses, new HttpHeaders(), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/viewOrdersBy/{status}/{pageNo}")
    public ResponseEntity<Page<OrderResponse>> viewOrdersByStatus(@PathVariable(value = "status") ORDER_STATUS status, @PathVariable(value = "pageNo") int pageNo) {
        final Page<OrderResponse> ordersByStatus = orderService.getOrdersByStatus(status, pageNo);
        return ResponseEntity.ok(ordersByStatus);
    }
}
