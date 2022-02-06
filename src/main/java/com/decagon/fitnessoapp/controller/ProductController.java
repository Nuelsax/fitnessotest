package com.decagon.fitnessoapp.controller;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.dto.UserProductDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping("/viewproducts/{pageNumber}")
    public ResponseEntity<?> viewAllProducts(@PathVariable(value="pageNumber") int pageNumber) {
        final Page<UserProductDto> allProducts = productService.getAllProducts(pageNumber);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/search/{freeText}")
    public ResponseEntity<List<?>> searchProduct(@PathVariable String freeText){
        List<?> products = productService.searchProduct(freeText);
        if(products.isEmpty()){
            return new ResponseEntity<>(List.of("Product not found"), HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(products, HttpStatus.OK);
    }
}
