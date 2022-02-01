package com.decagon.fitnessoapp.service;

<<<<<<< HEAD
import com.decagon.fitnessoapp.dto.ProductDetailsRequest;
import com.decagon.fitnessoapp.dto.ProductDetailsResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ProductDetailsResponse> viewProductDetails(Long id);
=======
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
>>>>>>> 4d41cf89da0bf069c1a0049ab6a69b3d18daa541
}
