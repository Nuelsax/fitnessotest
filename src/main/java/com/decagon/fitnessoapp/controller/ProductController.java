package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.addProduct(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable (name = "id") Long productId, @RequestBody ProductRequestDto requestDto){
        return productService.updateProduct(productId, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId){
        return productService.getProduct(productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allproducts/{size}/{number}")
    public ResponseEntity<Page<TangibleProduct>> getAllProduct(@PathVariable (name = "size") int pageSize,@PathVariable (name = "number") int pageNumber){
        return productService.getAllProduct(pageSize, pageNumber);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allservices/{size}/{number}")
    public ResponseEntity<Page<IntangibleProduct>> getAllServices(@PathVariable (name = "size") int pageSize, @PathVariable (name = "number") int pageNumber){
        return productService.getAllServices(pageSize, pageNumber);
    }

    @GetMapping("/viewproductdetails/{productId}/{ProductType}")
    public ResponseEntity<ProductResponseDto> viewProductDetail(@PathVariable("productId") Long productId,
                                                                  @PathVariable("ProductType") String productType){
        ProductResponseDto productResponseDto = productService.viewProductDetails(productId, productType).getBody();
        productResponseDto.setStock(null);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/viewproductdetails/{Id}/{producttype}")
    public ResponseEntity<ProductResponseDto> viewProductDetailForAdmin(@PathVariable("Id") Long productId,
                                                                  @PathVariable("producttype") String productType){
        return productService.viewProductDetails(productId, productType);
    }
}
