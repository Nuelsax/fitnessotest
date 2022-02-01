package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ProductDetailsRequest;
import com.decagon.fitnessoapp.dto.ProductDetailsResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ProductDetailsResponse> viewProductDetails(Long id);
}
