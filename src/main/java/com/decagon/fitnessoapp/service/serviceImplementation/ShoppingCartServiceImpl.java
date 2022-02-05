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
    public Cart addToCart(Long productId, int quantity, PersonDetails authentication) {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);

        Person person = personRepository.findPersonByUserName(authentication.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("User Name does not Exist"));


        Cart cart = getCarts(person);
        if(tangibleProduct.isPresent()) {
            TangibleProduct product = mapper.convertValue(tangibleProduct, TangibleProduct.class);
            cart.getTangibleProduct().putIfAbsent(product, quantity);
        } else if(intangibleProduct.isPresent()) {
            IntangibleProduct product = mapper.convertValue(intangibleProduct, IntangibleProduct.class);
            cart.getIntangibleProduct().putIfAbsent(product, quantity);
        }

        System.out.println("The cart: " + cart);
        return shoppingCartRepository.save(cart);
    }

    @NotNull
    private Cart getCarts(Person person) {
        Cart cart = shoppingCartRepository.findByPerson(person).orElse(null);
        Cart newCart = new Cart();
        if(cart == null) {
            newCart.setPerson(person);
            return newCart;
        } else return cart;
    }

   /* private Cart addTangibleToCart(TangibleProduct product, int quantity, Cart cart) {
        String sku = product.getStockKeepingUnit();
        if(cart.getTangibleProducts() != null) {
            final long count = cart.getTangibleProducts()
                    .stream()
                    .filter(x -> x.getStockKeepingUnit().equals(sku))
                    .count();
            if(count > 0) {
                Integer currQuantity = cart.getQuantity();
                cart.setQuantity(currQuantity + quantity);
            }
        } else {
            Set<TangibleProduct> cartList = new HashSet<>();
            cartList.add(product);
            cart.setTangibleProducts(cartList);
            cart.setQuantity(quantity);
        }
        return shoppingCartRepository.save(cart);
    }

    private Cart addIntangibleToCart(IntangibleProduct product, int quantity, Cart cart) {
        String sku = product.getStockKeepingUnit();
        if(cart.getIntangibleProducts() != null) {
            final long count = cart.getIntangibleProducts()
                    .stream()
                    .filter(x -> x.getStockKeepingUnit().equals(sku))
                    .count();
            if(count > 0) {
                Integer currQuantity = cart.getQuantity();
                cart.setQuantity(currQuantity + quantity);
            }
        } else {
            Set<IntangibleProduct> cartList = new HashSet<>();
            cartList.add(product);
            cart.setIntangibleProducts(cartList);
            cart.setQuantity(quantity);
        }
        return shoppingCartRepository.save(cart);
    }
*/
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

