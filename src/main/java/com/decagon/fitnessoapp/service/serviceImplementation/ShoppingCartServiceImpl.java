package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.model.product.ShoppingItem;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<ShoppingItem> addProductAsShoppingItem(Long productId, int quantity) {
        boolean isTangibleProduct = tangibleProductRepository.findById(productId).isPresent();
        boolean isInTangibleProduct = intangibleProductRepository.findById(productId).isPresent();

        ShoppingItem savedItem = null;
        if (isTangibleProduct || isInTangibleProduct) {
            ShoppingItem shoppingItem = new ShoppingItem(intangibleProductRepository.getById(productId),tangibleProductRepository.getById(productId), quantity);

            savedItem = shoppingCartRepository.save(shoppingItem);
        }
        return ResponseEntity.ok().body(savedItem);
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

