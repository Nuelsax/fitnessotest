package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public ResponseEntity<List<Optional<? extends Product>>> addProductAsShoppingItem(Long productId, int quantity) {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<TangibleProduct> intangibleProduct = tangibleProductRepository.findById(productId);

        List<Optional<? extends Product>> cart = new ArrayList<>();

        if(tangibleProduct.isPresent()) {
            cart.add(tangibleProduct);
        }
        if (intangibleProduct.isPresent()) {
            cart.add(intangibleProduct);
        }


        /*hoppingItem savedItem = null;
        if (isTangibleProduct || isInTangibleProduct) {

            cart.add((ProductResponseDto) )
            ShoppingItem shoppingItem = new ShoppingItem(intangibleProductRepository.getById(productId),tangibleProductRepository.getById(productId), quantity);

            savedItem = shoppingCartRepository.save(shoppingItem);
        }*/
        return ResponseEntity.ok().body(cart);
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

