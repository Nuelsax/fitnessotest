package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.CartResponse;
import com.decagon.fitnessoapp.model.product.CHANGE_QUANTITY;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.security.JwtUtils;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final PersonDetailsService userService;
    private final JwtUtils jwtTokenUtils;

   @PutMapping("/add/{productId}/{status}")
    public ResponseEntity<?> addItemToCart(@PathVariable(value = "productId") Long productId, @PathVariable(value = "status") CHANGE_QUANTITY status, HttpServletRequest request) throws Exception {
       final String requestHeader = request.getHeader("Authorization");
       String jwt = requestHeader.substring(7);
       String username = jwtTokenUtils.extractUsername(jwt);
       final PersonDetails personDetails = userService.loadUserByUsername(username);

       return ResponseEntity.ok(shoppingCartService.addToCart(productId, status, personDetails));
   }

   @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable(value = "productId") Long productId, HttpServletRequest request) throws Exception {
        final String requestHeader = request.getHeader("Authorization");
        String jwt = requestHeader.substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        final PersonDetails personDetails = userService.loadUserByUsername(username);
       return ResponseEntity.ok(shoppingCartService.removeFromCart(productId, personDetails));
   }

   @GetMapping("/viewCartItems/{id}")
   public ResponseEntity<CartResponse> getCartById(@PathVariable(value = "id") Long productId){
       return ResponseEntity.ok(shoppingCartService.getCartById(productId));
   }

    @GetMapping("/viewCartItems")
    public ResponseEntity<CartResponse> viewCartItems(){
        return ResponseEntity.ok(shoppingCartService.viewCartItems());
    }
}
