package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.CheckOutRequest;
import com.decagon.fitnessoapp.dto.CheckOutResponse;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.model.product.CheckOut;
import com.decagon.fitnessoapp.model.product.CouponCode;
import com.decagon.fitnessoapp.model.product.ShoppingItem;
import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.PaymentCard;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.CheckOutRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.ShoppingCartRepository;
import com.decagon.fitnessoapp.service.CheckOutService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckOutRepository checkOutRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final ShoppingCartRepository shoppingCartRepository;


    @Override
    public CheckOutResponse save(CheckOutRequest checkOutRequest) {
        CheckOut checkOut = new CheckOut();
        Address billingAddress = new Address();
        Address shippingAddress = new Address();
        PaymentCard paymentCard = new PaymentCard();
        CouponCode couponCode = new CouponCode();
        Optional<Person> personExists = personRepository.findByEmail(checkOutRequest.getEmail());
        Optional<ShoppingItem> shoppingItemExist = shoppingCartRepository.findById(checkOutRequest.getShoppingCartId());
        if(personExists.isPresent()){
            if(shoppingItemExist.isPresent()){
                checkOut.setPerson(personExists.get());
                billingAddress.setPerson(personExists.get());
                shippingAddress.setPerson(personExists.get());
                paymentCard.setPerson(personExists.get());
                modelMapper.map(checkOutRequest.getPaymentRequest(), paymentCard);
                modelMapper.map(checkOutRequest.getBillingAddress(), billingAddress);
                modelMapper.map(checkOutRequest.getShippingAddress(), shippingAddress);
                modelMapper.map(checkOutRequest.getDiscountRequest(), couponCode);
                checkOut.setTotalPrice(checkOutRequest.getOrderSummary().getTotal());
                checkOut.setBillingAddress(billingAddress);
                checkOut.setShippingAddress(shippingAddress);
                checkOut.setShoppingItems(shoppingItemExist.get());
                checkOut.setShippingMethod(checkOutRequest.getShippingMethod());
                checkOut.setCouponCode(couponCode);
                checkOut.setPaymentCard(paymentCard);
                checkOutRepository.save(checkOut);
                //TODO: Confirm payment card Details
                return CheckOutResponse.builder().message("Check-out Successful").timestamp(LocalDateTime.now()).build();
            }
            return CheckOutResponse.builder().message("Shopping cart is empty").timestamp(LocalDateTime.now()).build();

        }
        return CheckOutResponse.builder().message("User not Registered").timestamp(LocalDateTime.now()).build();
    }
}
