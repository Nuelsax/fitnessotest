package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

   @PutMapping("/{productId}")
    public ResponseEntity<?> addItemToCart(@PathVariable(value = "productId") Long productId, int quantity) {
       return ResponseEntity.ok(shoppingCartService.addProductAsShoppingItem(productId, quantity));
   }

   @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable(value = "productId") Long productId){
       return ResponseEntity.ok(shoppingCartService.removeProductAsShoppingItem(productId));
   }
}