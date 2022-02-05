package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {

     List<Cart> addToCart(Long productId, int quantity, PersonDetails authentication);

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
}
