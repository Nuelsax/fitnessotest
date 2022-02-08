package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ShoppingItemResponse;
import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {

     Cart addToCart(Long productId, int quantity, PersonDetails authentication) throws Exception;

     ResponseEntity<String> removeProductAsShoppingItem(Long productId);
     List<Cart> viewCartItems();

     ShoppingItemResponse getCartById(Long productId);


}
