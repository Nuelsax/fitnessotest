package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ProductDetailsRequest;
import com.decagon.fitnessoapp.dto.ProductDetailsResponse;
import com.decagon.fitnessoapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/view/productDetails/{id}")
    private ResponseEntity<ProductDetailsResponse> viewProductDetails(@PathVariable("id") Long id){
        return productService.viewProductDetails(id);
    }
}
