package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.ProductDetailsRequest;
import com.decagon.fitnessoapp.dto.ProductDetailsResponse;
import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
