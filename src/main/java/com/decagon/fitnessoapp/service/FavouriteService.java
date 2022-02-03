package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.FavouriteResponse;
import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.user.Favourite;
import com.decagon.fitnessoapp.model.user.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FavouriteService {
    ResponseEntity<String> addOrDeleteFavourite(Long productId, Authentication authentication);

    FavouriteResponse viewFavourites(Person person, Authentication authentication);
}
