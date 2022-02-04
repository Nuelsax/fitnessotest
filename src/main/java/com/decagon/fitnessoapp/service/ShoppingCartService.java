package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.product.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {


     ResponseEntity<List<Optional<? extends Product>>> addProductAsShoppingItem(Long productId, int quantity);

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
}
