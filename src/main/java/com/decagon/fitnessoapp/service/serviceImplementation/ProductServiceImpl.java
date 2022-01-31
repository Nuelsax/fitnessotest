package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final TangibleProductRepository  tangibleProductRepository;
    private final IntangibleProductRepository intangibleProductRepository;
    private ModelMapper mapper;

    public ProductServiceImpl(TangibleProductRepository tangibleProductRepository, IntangibleProductRepository intangibleProductRepository, ModelMapper mapper) {
        this.tangibleProductRepository = tangibleProductRepository;
        this.intangibleProductRepository = intangibleProductRepository;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto) {
        Product newProduct;
        ProductResponseDto responseDto;
        if (requestDto.getProductType().equals("PRODUCT")) {
            newProduct = tangibleProductRepository.save(mapper.map(requestDto, TangibleProduct.class));
            responseDto = mapper.map(newProduct, ProductResponseDto.class);
        } else if (requestDto.getProductType().equals("SERVICE")) {
            newProduct = intangibleProductRepository.save(mapper.map(requestDto, IntangibleProduct.class));
            responseDto = mapper.map(newProduct, ProductResponseDto.class);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            return ResponseEntity.ok(responseDto);
    }



    @Override
    public ResponseEntity<ProductResponseDto> deleteProduct(Long productId) {
        boolean isThere = tangibleProductRepository.findById(productId).isPresent();
        boolean isThere2 = intangibleProductRepository.findById(productId).isPresent();

        if(isThere) {
            TangibleProduct deletedProduct =  tangibleProductRepository.getById(productId);
            tangibleProductRepository.deleteById(productId);
            return ResponseEntity.ok(mapper.map(deletedProduct, ProductResponseDto.class));
        }

        if(isThere2) {
            IntangibleProduct deletedProduct =  intangibleProductRepository.getById(productId);
            intangibleProductRepository.deleteById(productId);
            return ResponseEntity.ok(mapper.map(deletedProduct, ProductResponseDto.class));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        return  null;
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getAllServices() {
        return null;
    }


    @Override
    public ResponseEntity<ProductResponseDto> updateProduct(Long productId, ProductRequestDto requestDto) {
        boolean isThere = tangibleProductRepository.findById(productId).isPresent();
        boolean isThere2 = intangibleProductRepository.findById(productId).isPresent();

        if(isThere){
            TangibleProduct product = tangibleProductRepository.getById(productId);
            mapper.map(requestDto, product);
            TangibleProduct updatedProduct = tangibleProductRepository.save(product);
            return ResponseEntity.ok(mapper.map(updatedProduct, ProductResponseDto.class));
        }
        if(isThere2){
            IntangibleProduct product = intangibleProductRepository.getById(productId);
            mapper.map(requestDto, product);
            IntangibleProduct updatedProduct = intangibleProductRepository.save(product);
            return ResponseEntity.ok(mapper.map(updatedProduct, ProductResponseDto.class));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ProductResponseDto> getProduct(Long productId) {
        boolean isThere = tangibleProductRepository.findById(productId).isPresent();
        boolean isThere2 = intangibleProductRepository.findById(productId).isPresent();

        if(isThere){
            ProductResponseDto responseDto = new ProductResponseDto();
            mapper.map(responseDto, tangibleProductRepository.getById(productId));
            return ResponseEntity.ok(responseDto);

        }

        if(isThere2){
            ProductResponseDto responseDto = new ProductResponseDto();
            mapper.map(responseDto, intangibleProductRepository.getById(productId));
            return ResponseEntity.ok(responseDto);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
