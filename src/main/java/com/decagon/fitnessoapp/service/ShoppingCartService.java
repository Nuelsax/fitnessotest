package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.product.ShoppingItem;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ShoppingCartService {


     ResponseEntity<ShoppingItem> addProductAsShoppingItem(Long productId, int quantity);

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
}
