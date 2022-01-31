package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.service.serviceImplementation.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto requestDto) {
         return productService.addProduct(requestDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductResponseDto> deleteProduct(Long productId){
        return productService.deleteProduct(productId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable (name = "id") Long productId, @RequestBody ProductRequestDto requestDto){
        return productService.updateProduct(productId, requestDto);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable ("id") Long productId){
        return productService.getProduct(productId);
    }

    @GetMapping("/products/{size}/{number}")
    public ResponseEntity<Page<TangibleProduct>> getAllProduct(@PathVariable (name = "size") int pageSize,@PathVariable (name = "number") int pageNumber){
        return productService.getAllProduct(pageSize, pageNumber);
    }

    @GetMapping("/products/{size}/{number}")
    public ResponseEntity<Page<IntangibleProduct>> getAllServices(@PathVariable (name = "size") int pageSize, @PathVariable (name = "number") int pageNumber){
        return productService.getAllServices(pageSize, pageNumber);
    }
}
