package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.ProductDetailsResponse;
import com.decagon.fitnessoapp.dto.ProductRequestDto;
import com.decagon.fitnessoapp.dto.ProductResponseDto;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

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
    public ResponseEntity<ProductDetailsResponse> viewProductDetails(Long id) {
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();

        modelMapper.map(intangibleProductRepository.getById(id), productDetailsResponse);
        System.out.println(productDetailsResponse);
        if(productDetailsResponse != null){
            if(productDetailsResponse.getStockKeepingUnit().equals("0")) {
                productDetailsResponse.setImage("Sold Out");
            }
            System.out.println("Enter");
            return ResponseEntity.ok().body(productDetailsResponse);
        }
        modelMapper.map(productDetailsResponse, tangibleProductRepository.getById(id));
        if(productDetailsResponse.getStockKeepingUnit().equals("0")) {
            productDetailsResponse.setImage("Sold Out");
        }
        System.out.println("Enter2");
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
}
