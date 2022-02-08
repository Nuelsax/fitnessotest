package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FavouriteService {
    ResponseEntity<String> addOrDeleteFavourite(Long productId, Authentication authentication);

    List<ProductResponseDto> viewFavourites(Authentication authentication);
}
