package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import org.springframework.http.ResponseEntity;

public interface ShoppingCartService {

     Cart addToCart(Long productId, int quantity, PersonDetails authentication) throws Exception;

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
}
