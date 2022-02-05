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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


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
    public List<Cart> addToCart(Long productId, int quantity, PersonDetails authentication) {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);

        Person person = personRepository.findPersonByUserName(authentication.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("User Name does not Exist"));

        Cart cart = shoppingCartRepository.findByPerson(person.getId()).orElse(null);
        if (cart == null) cart.setPerson(person);
        Cart addedItem = new Cart();

        if(tangibleProduct.isPresent()) {
            TangibleProduct product = mapper.convertValue(tangibleProduct, TangibleProduct.class);
            addedItem = addTangibleToCart(product, quantity, cart);
        } else if(intangibleProduct.isPresent()) {
            IntangibleProduct product = mapper.convertValue(intangibleProduct, IntangibleProduct.class);
            addedItem = addIntangibleToCart(product, quantity, cart);
        }

        return shoppingCartRepository.findAll();
    }

    private Cart addTangibleToCart(TangibleProduct product, int quantity, Cart cart) {
        String s = product.getStockKeepingUnit();
        if(cart.getTangibleProducts().size() > 0) {
            final long count = cart.getTangibleProducts()
                    .stream()
                    .filter(x -> x.getStockKeepingUnit().equals(s))
                    .count();
            if(count > 0) {
                Integer currQuantity = cart.getQuantity();
                cart.setQuantity(currQuantity + quantity);
            }
        } else if (cart.getTangibleProducts().isEmpty()) {
            cart.setTangibleProducts(Collections.singletonList(product));
        }
        return shoppingCartRepository.save(cart);
    }

    private Cart addIntangibleToCart(IntangibleProduct product, int quantity, Cart cart) {
        String s = product.getStockKeepingUnit();
        if(cart.getIntangibleProducts().size() > 0) {
            final long count = cart.getIntangibleProducts()
                    .stream()
                    .filter(x -> x.getStockKeepingUnit().equals(s))
                    .count();
            if(count > 0) {
                Integer currQuantity = cart.getQuantity();
                cart.setQuantity(currQuantity + quantity);
            }
        } else if (cart.getIntangibleProducts().isEmpty()) {
            cart.setIntangibleProducts(Collections.singletonList(product));
        }
        return shoppingCartRepository.save(cart);
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


// List<Optional<? extends Product>> cart = new ArrayList<>();
        /*if (tangibleProduct.isPresent() && cart.getTangibleProducts().isEmpty()) {
            cart.setTangibleProducts(Collections.singletonList(good));
        } else if(intangibleProduct.isPresent() && cart.getIntangibleProducts().isEmpty()) {
            cart.setIntangibleProducts(Collections.singletonList(service));
        }
        if(tangibleProduct.isPresent() && cart.getTangibleProducts().contains(good)) {
            cart.getTangibleProducts().forEach(x -> x.getStockKeepingUnit().equals(good.getStockKeepingUnit()));
        }

            if(cart.getTangibleProducts().contains(good)) {

            }
            cartItems.setTangibleProducts(Collections.singletonList(tang));
            cartItems.setQuantity(quantity);
            shoppingCartRepository.save(cartItems);
        } else if (intangibleProduct.isPresent()) {

            cartItems.setQuantity(quantity);
            cartItems.setIntangibleProducts(Collections.singletonList(intang));
              .save(cartItems);
        }
*/
        /*hoppingItem savedItem = null;
        if (isTangibleProduct || isInTangibleProduct) {

            cart.add((ProductResponseDto) )
            ShoppingItem shoppingItem = new ShoppingItem(intangibleProductRepository.getById(productId),tangibleProductRepository.getById(productId), quantity);

            savedItem = shoppingCartRepository.save(shoppingItem);
        }*/