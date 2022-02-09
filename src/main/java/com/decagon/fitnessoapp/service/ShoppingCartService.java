package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ShoppingItemResponse;
import com.decagon.fitnessoapp.model.product.CHANGE_QUANTITY;
import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.service.serviceImplementation.PersonDetails;

import java.util.List;

public interface ShoppingCartService {

     ShoppingItemResponse addToCart(Long productId, CHANGE_QUANTITY state, PersonDetails authentication) throws Exception;

     ShoppingItemResponse removeFromCart(Long productId, PersonDetails authentication);

     Cart getCartById(Long productId);

     List<Cart> viewCartItems();
}
