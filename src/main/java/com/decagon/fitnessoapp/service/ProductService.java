package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto);

    ResponseEntity<ProductResponseDto> deleteProduct(Long id);

    ResponseEntity<List<ProductResponseDto>> getAllProduct();

    ResponseEntity<List<ProductResponseDto>> getAllServices();

    ResponseEntity<ProductResponseDto> updateProduct(Long id,  ProductRequestDto requestDto);

    ResponseEntity<ProductResponseDto> getProduct(Long id);
}
