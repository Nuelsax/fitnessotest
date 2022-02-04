package com.decagon.fitnessoapp.service;

import org.springframework.http.ResponseEntity;

public interface ShoppingCartService {


     ResponseEntity<?> addProductAsShoppingItem(Long productId, int quantity);

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
}
