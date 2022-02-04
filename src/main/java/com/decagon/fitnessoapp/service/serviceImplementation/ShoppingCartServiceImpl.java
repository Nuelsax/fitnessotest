package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.CartProductDto;
import com.decagon.fitnessoapp.model.product.*;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@NoArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private ShoppingCartRepository shoppingCartRepository;

    private TangibleProductRepository tangibleProductRepository;

    private IntangibleProductRepository intangibleProductRepository;

    private PersonRepository personRepository;

    private ObjectMapper mapper;

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
    public ResponseEntity<?> addToCart(Long productId, int quantity, Authentication authentication) {
        Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
        Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);

        Person person = personRepository.findPersonByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User Name does not Exist"));

        Cart cart = shoppingCartRepository.findByPerson(person.getId()).orElse(null);
        if (cart == null) {
            cart.setPerson(person);
        }
       //CartProductDto product = new CartProductDto();
        if(tangibleProduct.isPresent()) {
            TangibleProduct product = mapper.convertValue(tangibleProduct, TangibleProduct.class);
            addTangibleToCart(product, quantity, cart);
        } else if(intangibleProduct.isPresent()) {
            IntangibleProduct product = mapper.convertValue(intangibleProduct, IntangibleProduct.class);
            addIntangibleToCart(product, quantity, cart);
        }

        return ResponseEntity.ok().body(shoppingCartRepository.findAll());
    }

    @Override
    public Cart addTangibleToCart(TangibleProduct product, int quantity, Cart cart) {
        int additionalQuantity = quantity;
        //Getting the products from the database for both tangible and intangible
        String s = product.getStockKeepingUnit();

       // ShoppingItem cartItem = shoppingCartRepository.findById(productId).get() ;
        //Validating if item is in the cart
        if (cart.getTangibleProducts().isEmpty()) {
            cart.setTangibleProducts(Collections.singletonList(product));
            /*additionalQuantity = cart.getQuantity() + quantity;
            cart.setQuantity(additionalQuantity);*/
        } else if(cart.getTangibleProducts().forEach(x -> x.getStockKeepingUnit().equals(x))) {
            additionalQuantity = cart.getTangibleProducts(). + quantity;
//            cartItem.setQuantity(additionalQuantity);*/
//            cartItem = new ShoppingItem();
//            cartItem.setQuantity(additionalQuantity);
//            cartItem.setIntangibleProducts((List<IntangibleProduct>) isIntangible);
//            cartItem.setTangibleProducts((List<TangibleProduct>) isTangible);
        }
        return shoppingCartRepository.save(cart);
    }

    @Override
    public Cart addIntangibleToCart(IntangibleProduct product, int quantity, Cart cart) {int additionalQuantity = quantity;
        //Getting the products from the database for both tangible and intangible
        String s = product.getStockKeepingUnit();

        // ShoppingItem cartItem = shoppingCartRepository.findById(productId).get() ;
        //Validating if item is in the cart
        if (cart.getIntangibleProducts().isEmpty()) {
            cart.setIntangibleProducts(Collections.singletonList(product));
            /*additionalQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(additionalQuantity);*/
        } else if(cart.getIntangibleProducts().forEach(x -> x.getStockKeepingUnit().equals(x))) {
            additionalQuantity = cart.getTangibleProducts(). + quantity;
//            cartItem.setQuantity(additionalQuantity);*/
//            cartItem = new ShoppingItem();
//            cartItem.setQuantity(additionalQuantity);
//            cartItem.setIntangibleProducts((List<IntangibleProduct>) isIntangible);
//            cartItem.setTangibleProducts((List<TangibleProduct>) isTangible);
        }

        return shoppingCartRepository.save(cart);;
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