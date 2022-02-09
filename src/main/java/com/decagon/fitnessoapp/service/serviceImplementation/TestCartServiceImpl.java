package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.CartResponse;
import com.decagon.fitnessoapp.dto.transactionDto.response.TestCartResponse;
import com.decagon.fitnessoapp.model.product.*;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TestCartServiceImpl {

    private final TestCartRepository testCartRepository;

    private final PersonRepository personRepository;

    private TangibleProductRepository tangibleProductRepository;

    private IntangibleProductRepository intangibleProductRepository;


    public TestCartResponse addToCart(Map<Long, Integer> products, PersonDetails personDetails) {

        Person person = personRepository.findPersonByUserName(personDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Name does not Exist"));

        for(Long productId : products.keySet()){
            Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(productId);
            Optional<TangibleProduct> tangibleProduct = tangibleProductRepository.findById(productId);
            if(tangibleProduct.isPresent()) {
                if(tangibleProduct.get().getStock() == 0){
                    return TestCartResponse.builder()
                            .message("Out of Stock for now").build();
                }
                else if(tangibleProduct.get().getStock() < products.get(productId)){
                    return TestCartResponse.builder()
                            .message("Exceeded what is available, currently we have "
                                    + tangibleProduct.get().getStock() + " of " + tangibleProduct.get().getProductName()
                            ).build();
                }

            }else if (intangibleProduct.isEmpty()) {
                return TestCartResponse.builder()
                        .message("Product with ID: " + productId + " does not exist")
                        .build();
            }
        }

        final String uniqueIdGenerator = RandomString.make(12);
        List<TestCart> all = new ArrayList<>();
        for(Map.Entry<Long, Integer> val : products.entrySet() ){
            TestCart testCart = new TestCart();
            testCart.setProductId(val.getKey());
            testCart.setQuantity(val.getValue());
            testCart.setUniqueCartId(uniqueIdGenerator);
            testCart.setPersonId(person.getId());
            updateTangibleProduct(val.getKey(), val.getValue());
            all.add(testCart);
        }


        return TestCartResponse.builder()
                .cartList(testCartRepository.saveAll(all)).build();
    }


    public TestCartResponse deleteCart (Long cartId, PersonDetails personDetails){
        Person person = personRepository.findPersonByUserName(personDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Name does not Exist"));
        testCartRepository.deleteById(cartId);
        return TestCartResponse.builder().message("removed").build();
    }


    public void updateTangibleProduct(Long productId, Integer quantity){
        TangibleProduct tangibleProduct = tangibleProductRepository.findById(productId).orElse(null);
        if(tangibleProduct != null){
            final Long stock = tangibleProduct.getStock();
            tangibleProduct.setStock(stock - Long.valueOf(quantity));
            tangibleProductRepository.save(tangibleProduct);
        }
    }

    public TestCartResponse getCartById(Long cartId) {
        return TestCartResponse.builder().testCart(testCartRepository.getById(cartId)).build();
    }

    public TestCartResponse viewCartItems() {
        return TestCartResponse.builder().cartList(testCartRepository.findAll()).build();
    }
}
