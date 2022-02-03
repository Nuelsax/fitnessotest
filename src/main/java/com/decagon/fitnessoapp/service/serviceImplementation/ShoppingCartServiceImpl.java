package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.ShoppingItem;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final TangibleProductRepository tangibleProductRepository;

    private final IntangibleProductRepository intangibleProductRepository;

    @Override
    public ResponseEntity<ShoppingItem> addProductAsShoppingItem(Long productId, int quantity) {

        int additionalQuantity = quantity;

        //Getting the products from the database for both tangible and intangible

        TangibleProduct isTangible = tangibleProductRepository.findById(productId).get();
        IntangibleProduct isIntangible = intangibleProductRepository.findById(productId).get();

        ShoppingItem cartItem = shoppingCartRepository.findById(productId).get() ;

      //Validating if item is in the cart

        if (cartItem != null) {

            additionalQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(additionalQuantity);

        } else {
            cartItem = new ShoppingItem();
            cartItem.setQuantity(additionalQuantity);
            cartItem.setIntangibleProducts((List<IntangibleProduct>) isIntangible);
            cartItem.setTangibleProducts((List<TangibleProduct>) isTangible);
        }
        shoppingCartRepository.save(cartItem);
        return ResponseEntity.ok().body(cartItem);
    }














//        Optional<TangibleProduct> isTangibleProduct = tangibleProductRepository.findById(productId);
//        Optional<IntangibleProduct> isInTangibleProduct = intangibleProductRepository.findById(productId);
//
//        ShoppingItem savedItem = null;
//        ShoppingItem shoppingItem = new ShoppingItem(isInTangibleProduct.get(), isTangibleProduct.get(), quantity);
//        if (isTangibleProduct.isPresent()) {
//            List<TangibleProduct> tangibleProducts = tangibleProductRepository.findAll();
//            for (TangibleProduct product : tangibleProducts) {
//                if (product.getProductName().equalsIgnoreCase(isTangibleProduct.get().getProductName())) {
//                    product.setQuantity(product.getQuantity() + isTangibleProduct.get().getQuantity());
//                } else {
//                    product.setQuantity(product.getQuantity());
//                }
//                savedItem = shoppingCartRepository.save(shoppingItem);
//            }
//        }
//        if (isInTangibleProduct.isPresent()) {
//            List<IntangibleProduct> inTangibleProducts = intangibleProductRepository.findAll();
//            for (IntangibleProduct services : inTangibleProducts) {
//                if (services.getProductName().equalsIgnoreCase(isInTangibleProduct.get().getProductName())) {
//                    services.setDurationInHoursPerDay(services.getDurationInHoursPerDay() + isInTangibleProduct.get().getDurationInHoursPerDay());
//                    services.setDurationInDays(services.getDurationInDays() + isInTangibleProduct.get().getDurationInDays());
//                } else {
//                    services.setDurationInHoursPerDay(services.getDurationInHoursPerDay());
//                    services.setDurationInDays(services.getDurationInDays());
//                }
//
//                savedItem = shoppingCartRepository.save(shoppingItem);
//            }
//        }
//        return ResponseEntity.ok().body(savedItem);

//
//    @Override
//    public ResponseEntity<String> removeProductAsShoppingItem(Long productId) {
//        Optional<TangibleProduct> isTangibleProduct1 = tangibleProductRepository.findById(productId);
//
//        Optional<IntangibleProduct> isInTangibleProduct1 = intangibleProductRepository.findById(productId);
//
//        List<ShoppingItem> cartItems = shoppingCartRepository.findAll();
//
//        for (ShoppingItem s : cartItems) {
//            if (s.getTangibleProducts().getProductName().equalsIgnoreCase(isTangibleProduct1.get().getProductName())) {
//
//                s.setQuantity(s.getTangibleProducts().getQuantity() - isTangibleProduct1.get().getQuantity());
//            } else if (isInTangibleProduct1.isPresent() && s.getIntangibleProducts().getProductName().equalsIgnoreCase(isInTangibleProduct1.get().getProductName())) {
//
//                s.setQuantity(s.getIntangibleProducts().getDurationInHoursPerDay() - isInTangibleProduct1.get().getDurationInHoursPerDay());
//
//                s.setQuantity(s.getIntangibleProducts().getDurationInDays() - isInTangibleProduct1.get().getDurationInDays());
//            }
//            shoppingCartRepository.deleteById(productId);
//        }
//        return ResponseEntity.ok().body("item deleted successfully");
//    }
    @Override
    public ResponseEntity<String> removeProductAsShoppingItem(Long id){
        ShoppingItem cartitem1 = shoppingCartRepository.findById(id).get();
        if(cartitem1 != null){
            shoppingCartRepository.delete(cartitem1);
        }
        return ResponseEntity.ok().body("Cart item deleted successfully");
    }
}

