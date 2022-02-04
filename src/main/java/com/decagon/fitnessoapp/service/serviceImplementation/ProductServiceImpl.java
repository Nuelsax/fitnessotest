package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.dto.UserProductDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements com.decagon.fitnessoapp.service.ProductService {

    private final IntangibleProductRepository intangibleProductRepository;
    private final TangibleProductRepository tangibleProductRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(IntangibleProductRepository intangibleProductRepository, TangibleProductRepository tangibleProductRepository, ModelMapper modelMapper) {
        this.intangibleProductRepository = intangibleProductRepository;
        this.tangibleProductRepository = tangibleProductRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ProductResponseDto> viewProductDetails(Long id, String productType) {
        ProductResponseDto productDetailsResponse = new ProductResponseDto();

        if(productType.equals("PRODUCT")){
            modelMapper.map(tangibleProductRepository.getById(id), productDetailsResponse );
            productDetailsResponse.setProductType("PRODUCT");
            if(productDetailsResponse.getStock() == 0) {
                productDetailsResponse.setImage("Sold Out");
            }

        }else if(productType.equals("SERVICE")){
            modelMapper.map(intangibleProductRepository.getById(id), productDetailsResponse);
            productDetailsResponse.setProductType("SERVICE");
            if(productDetailsResponse.getStock() == 0) {
                productDetailsResponse.setImage("Sold Out");
            }

        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(productDetailsResponse);
    }

    @Override
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto) {

        ProductResponseDto responseDto;
        if (requestDto.getProductType().equals("PRODUCT")) {

            TangibleProduct newProduct;

            newProduct = tangibleProductRepository.save(modelMapper.map(requestDto, TangibleProduct.class));
            responseDto = modelMapper.map(newProduct, ProductResponseDto.class);


        } else if (requestDto.getProductType().equals("SERVICE")) {

            IntangibleProduct newProduct;

            newProduct = intangibleProductRepository.save(modelMapper.map(requestDto, IntangibleProduct.class));
            responseDto = modelMapper.map(newProduct, ProductResponseDto.class);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            return ResponseEntity.ok(responseDto);
    }



    @Override
    public ResponseEntity<ProductResponseDto> deleteProduct(Long productId) {
        boolean isTangiblePresent = tangibleProductRepository.findById(productId).isPresent();
        boolean isIntangiblePresent = intangibleProductRepository.findById(productId).isPresent();

        if(isTangiblePresent ) {
            TangibleProduct deletedProduct =  tangibleProductRepository.getById(productId);
            tangibleProductRepository.deleteById(productId);
            return ResponseEntity.ok().body(modelMapper.map(deletedProduct, ProductResponseDto.class));
        }

        if(isIntangiblePresent) {
            IntangibleProduct deletedProduct =  intangibleProductRepository.getById(productId);
            intangibleProductRepository.deleteById(productId);
            return ResponseEntity.ok().body(modelMapper.map(deletedProduct, ProductResponseDto.class));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Page<TangibleProduct>> getAllProduct(int pageSize, int pageNumber) {
        Page<TangibleProduct> products = tangibleProductRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return  ResponseEntity.ok().body(products);
    }


    @Override
    public ResponseEntity<Page<IntangibleProduct>> getAllServices(int pageSize, int pageNumber) {
        Page<IntangibleProduct> products = intangibleProductRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return  ResponseEntity.ok().body(products);
    }


    @Override
    public ResponseEntity<ProductResponseDto> updateProduct(Long productId, ProductRequestDto requestDto) {
        boolean isTangiblePresent = tangibleProductRepository.findById(productId).isPresent();
        boolean isIntangiblePresent = intangibleProductRepository.findById(productId).isPresent();

        if(isTangiblePresent){
            TangibleProduct product = tangibleProductRepository.getById(productId);
            modelMapper.map(requestDto, product);


            TangibleProduct updatedProduct = tangibleProductRepository.save(product);
            return ResponseEntity.ok().body(modelMapper.map(updatedProduct, ProductResponseDto.class));
        }
        if(isIntangiblePresent){
            IntangibleProduct product = intangibleProductRepository.getById(productId);
            modelMapper.map(requestDto, product);


            IntangibleProduct updatedProduct = intangibleProductRepository.save(product);
            return ResponseEntity.ok().body(modelMapper.map(updatedProduct, ProductResponseDto.class));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ProductResponseDto> getProduct(Long productId) {
        boolean isTangiblePresent = tangibleProductRepository.findById(productId).isPresent();
        boolean isIntangiblePresent = intangibleProductRepository.findById(productId).isPresent();

        if (isTangiblePresent) {
            ProductResponseDto responseDto = new ProductResponseDto();
            modelMapper.map(tangibleProductRepository.getById(productId), responseDto);
            return ResponseEntity.ok().body(responseDto);
        }

        if (isIntangiblePresent) {
            ProductResponseDto responseDto = new ProductResponseDto();
            modelMapper.map(intangibleProductRepository.getById(productId), responseDto);
            return ResponseEntity.ok().body(responseDto);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* ======================== */
//    public ResponseEntity<List<Page>> getAllProducts(int pageNumber) {
//        int pageSize = 5;
//        String sortBy = "productName";
//        Pageable productPage = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
//        Page<IntangibleProduct> intangibleProducts = intangibleProductRepository.findAll(productPage);
//        Page<TangibleProduct> tangibleProducts = tangibleProductRepository.findAll(productPage);
//        List<Page> listOfProducts = new ArrayList<>();
//        listOfProducts.add(intangibleProducts);
//        listOfProducts.add(tangibleProducts);
//        return new ResponseEntity<>(listOfProducts, HttpStatus.ACCEPTED);
//    }

    @Override
    public Page<UserProductDto> getAllProducts(int pageNumber) {
         List<UserProductDto> dtoList = getDtoList();

        int pageSize = 10;
        int skipCount = (pageNumber - 1) * pageSize;

        List<UserProductDto> activityPage = dtoList
                .stream()
                .skip(skipCount)
                .limit(pageSize)
                .collect(Collectors.toList());

        Pageable productPage = PageRequest.of(pageNumber, pageSize, Sort.by("productName").ascending());

        return (Page<UserProductDto>) new PageImpl(activityPage, productPage, dtoList.size());
    }

    private List<UserProductDto> getDtoList() {
        List<IntangibleProduct> intangibleProducts = intangibleProductRepository.findAll();
        List<TangibleProduct> tangibleProducts = tangibleProductRepository.findAll();

        List<UserProductDto> intangibleDtos = intangibleProducts.stream()
                .map(x -> modelMapper.map(x, UserProductDto.class))
                .collect(Collectors.toList());

        List<UserProductDto> tangibleDtos = tangibleProducts.stream()
                .map(x -> modelMapper.map(x, UserProductDto.class))
                .collect(Collectors.toList());

        Collections.sort(intangibleDtos);
        Collections.sort(tangibleDtos);

        List<UserProductDto> productDtos = new ArrayList<>(intangibleDtos);
        productDtos.addAll(tangibleDtos);

        return productDtos;
    }
}
