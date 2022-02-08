package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.model.product.CHANGE_QUANTITY;
import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {

     Cart addToCart(Long productId, CHANGE_QUANTITY state, PersonDetails authentication) throws Exception;

     Cart removeFromCart(Long productId, PersonDetails authentication);

     Cart getCartById(Long productId);

     List<Cart> viewCartItems();
}
