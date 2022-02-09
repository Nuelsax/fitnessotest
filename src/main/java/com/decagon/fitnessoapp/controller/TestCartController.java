package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.CartResponse;
import com.decagon.fitnessoapp.model.product.CHANGE_QUANTITY;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetailsService;
import com.decagon.fitnessoapp.service.serviceImplementation.TestCartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/test-carts")
@RequiredArgsConstructor
public class TestCartController {
    private final TestCartServiceImpl testCartService;
    private final PersonDetailsService userService;
    private final JwtUtils jwtTokenUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody Map<Long, Integer> products, HttpServletRequest request) throws Exception {
        final String requestHeader = request.getHeader("Authorization");
        String jwt = requestHeader.substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        final PersonDetails personDetails = userService.loadUserByUsername(username);

        return ResponseEntity.ok(testCartService.addToCart(products, personDetails));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable(value = "cartId") Long cartId, HttpServletRequest request) throws Exception {
        final String requestHeader = request.getHeader("Authorization");
        String jwt = requestHeader.substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        final PersonDetails personDetails = userService.loadUserByUsername(username);
        return ResponseEntity.ok(testCartService.deleteCart(cartId, personDetails));
    }

    @GetMapping("/viewCartItems/{id}")
    public ResponseEntity<?> getCartById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(testCartService.getCartById(id));
    }

    @GetMapping("/viewCartItems")
    public ResponseEntity<?> viewCartItems(){
        return ResponseEntity.ok(testCartService.viewCartItems());
    }
}
