package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.model.product.*;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@NoArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCartRepository shoppingCartRepository;

    private TangibleProductRepository tangibleProductRepository;

    private IntangibleProductRepository intangibleProductRepository;

    private PersonRepository personRepository;

    private ObjectMapper mapper;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   TangibleProductRepository tangibleProductRepository,
                                   IntangibleProductRepository intangibleProductRepository,
                                   PersonRepository personRepository,
                                   ObjectMapper mapper) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.tangibleProductRepository = tangibleProductRepository;
        this.intangibleProductRepository = intangibleProductRepository;
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    @Override
    public Cart addToCart(Long productId, int quantity, PersonDetails authentication) throws Exception {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);
        Person person = personRepository.findPersonByUserName(authentication.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Name does not Exist"));
        Cart cart = getCarts(person);
        if(tangibleProduct.isPresent()) {
            setTangibleMap(quantity,tangibleProduct, cart);
        } else if(intangibleProduct.isPresent()) {
            setIntangibleMap(quantity, intangibleProduct, cart);
        } else {
            throw new Exception("Out of stock");
        }
        System.out.println("The cart: " + cart);
        return shoppingCartRepository.save(cart);
    }

    @NotNull
    private Cart getCarts(Person person) {
        Cart cart = shoppingCartRepository.findByPerson(person).orElse(null);
        Cart newCart = new Cart();
        newCart.setQuantity(0);
        if (cart == null) {
            newCart.setPerson(person);
            return newCart;
        } else return cart;
    }

    private Cart setTangibleMap(int quantity, Optional<TangibleProduct> tangibleProduct, Cart cart) {
        Map<TangibleProduct, Integer> tangibleProductIntegerMap = cart.getTangibleProduct();
        TangibleProduct product = mapper.convertValue(tangibleProduct, TangibleProduct.class);
        String sku = product.getStockKeepingUnit();
        for (Map.Entry<TangibleProduct, Integer> entry : tangibleProductIntegerMap.entrySet()) {
            if (sku.equals(entry.getKey().getStockKeepingUnit())) {
                Integer currQuantity = entry.getValue();
                entry.setValue(currQuantity + quantity);
            } else {
                tangibleProductIntegerMap.put(product, quantity);
            }
        }
        cart.setTangibleProduct(tangibleProductIntegerMap);
        return cart;
    }

    private Cart setIntangibleMap(int quantity, Optional<IntangibleProduct> intangibleProduct, Cart cart) {
        Map<IntangibleProduct, Integer> intangibleProductIntegerMap = cart.getIntangibleProduct();
        IntangibleProduct product = mapper.convertValue(intangibleProduct, IntangibleProduct.class);
        String sku = product.getStockKeepingUnit();
        for (Map.Entry<IntangibleProduct, Integer> entry : intangibleProductIntegerMap.entrySet()) {
            if (sku.equals(entry.getKey().getStockKeepingUnit())) {
                Integer currQuantity = entry.getValue();
                entry.setValue(currQuantity + quantity);
            } else {
                intangibleProductIntegerMap.put(product, quantity);
            }
        }
        cart.setIntangibleProduct(intangibleProductIntegerMap);
        return cart;
    }

        @Override
        public ResponseEntity<String> removeProductAsShoppingItem (Long productId){
            boolean exists = shoppingCartRepository.findById(productId).isPresent();
            if (!exists) {
                throw new IllegalStateException("Product with id " + productId + " does not exist");
            }
            shoppingCartRepository.deleteById(productId);
            return ResponseEntity.ok("Product: " + productId + " has been deleted successfully");
        }
}

/*
*  if(tangibleProduct.isPresent()) {
            TangibleProduct product = mapper.convertValue(tangibleProduct, TangibleProduct.class);
            Map<TangibleProduct, Integer> tangibleProductIntegerMap = cart.getTangibleProduct();

            String sku = product.getStockKeepingUnit();
            //if(sku.equals(tangibleProductIntegerMap.entrySet().))
            for (Map.Entry<TangibleProduct, Integer> entry : tangibleProductIntegerMap.entrySet()) {
                if (sku.equals(entry.getKey().getStockKeepingUnit())) {
                    Integer currQuantity = entry.getValue();
                    entry.setValue(currQuantity + quantity);
                } else {
                    tangibleProductIntegerMap.put(product, quantity);
                }
            }
            cart.setTangibleProduct(tangibleProductIntegerMap);
        }

        if(intangibleProduct.isPresent()) {
            IntangibleProduct product = mapper.convertValue(intangibleProduct, IntangibleProduct.class);
            Map<IntangibleProduct, Integer> intangibleProductIntegerMap = cart.getIntangibleProduct():

            String sku = product.getStockKeepingUnit();
            //if(sku.equals(tangibleProductIntegerMap.entrySet().))
            for (Map.Entry<TangibleProduct, Integer> entry : tangibleProductIntegerMap.entrySet()) {
                if (sku.equals(entry.getKey().getStockKeepingUnit())) {
                    Integer currQuantity = entry.getValue();
                    entry.setValue(currQuantity + quantity);
                } else {
                    tangibleProductIntegerMap.put(product, quantity);
                }
            }
        }
* */