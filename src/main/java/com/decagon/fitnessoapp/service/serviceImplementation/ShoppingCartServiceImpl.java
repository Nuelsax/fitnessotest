package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.product.ShoppingItem;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@NoArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private TangibleProductRepository tangibleProductRepository;

    @Autowired
    private IntangibleProductRepository intangibleProductRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public ResponseEntity<?> addProductAsShoppingItem(Long productId, int quantity) {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);
        TangibleProduct tang = mapper.convertValue(tangibleProduct, TangibleProduct.class);
        IntangibleProduct intang = mapper.convertValue(intangibleProduct, IntangibleProduct.class);


       // List<Optional<? extends Product>> cart = new ArrayList<>();
        ShoppingItem cartItems = new ShoppingItem();

        if(tangibleProduct.isPresent()) {
           // cart.add(tangibleProduct);
            cartItems.setTangibleProducts(Collections.singletonList(tang));
            cartItems.setQuantity(quantity);
            shoppingCartRepository.save(cartItems);
        } else if (intangibleProduct.isPresent()) {

            cartItems.setQuantity(quantity);
            cartItems.setIntangibleProducts(Collections.singletonList(intang));
            shoppingCartRepository.save(cartItems);
        }

        /*hoppingItem savedItem = null;
        if (isTangibleProduct || isInTangibleProduct) {

            cart.add((ProductResponseDto) )
            ShoppingItem shoppingItem = new ShoppingItem(intangibleProductRepository.getById(productId),tangibleProductRepository.getById(productId), quantity);

            savedItem = shoppingCartRepository.save(shoppingItem);
        }*/
        return ResponseEntity.ok().body(shoppingCartRepository.findAll());
    }

    @Override
    public ResponseEntity<String> removeProductAsShoppingItem(Long productId) {
        boolean exists = shoppingCartRepository.findById(productId).isPresent();

        if (!exists) {
            throw new IllegalStateException("Product with id "+ productId + " does not exist");
        }

        shoppingCartRepository.deleteById(productId);

        return ResponseEntity.ok("Product: " + productId + " has been deleted successfully");
    }
}