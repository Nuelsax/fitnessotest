package com.decagon.fitnessoapp.controller;

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

   @PutMapping("/add/{productId}/{quantity}")
    public ResponseEntity<?> addItemToCart(@PathVariable(value = "productId") Long productId, @PathVariable(value = "quantity") int quantity, HttpServletRequest request) throws Exception {
       final String requestHeader = request.getHeader("Authorization");
       String jwt = requestHeader.substring(7);
       String username = jwtTokenUtils.extractUsername(jwt);
       final PersonDetails personDetails = userService.loadUserByUsername(username);

       return ResponseEntity.ok(shoppingCartService.addToCart(productId, quantity, personDetails));
   }

   @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable(value = "productId") Long productId){
       return ResponseEntity.ok(
               shoppingCartService.removeProductAsShoppingItem(productId));
   }
}