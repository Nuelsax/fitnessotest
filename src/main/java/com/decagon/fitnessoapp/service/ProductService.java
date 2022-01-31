package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto);

    ResponseEntity<ProductResponseDto> deleteProduct(Long id);

    ResponseEntity<Page<TangibleProduct>> getAllProduct(int pageSize, int pageNumber);

    ResponseEntity<Page<IntangibleProduct>> getAllServices(int pageSize, int pageNumber);

    ResponseEntity<ProductResponseDto> updateProduct(Long id,  ProductRequestDto requestDto);

    ResponseEntity<ProductResponseDto> getProduct(Long id);
}
