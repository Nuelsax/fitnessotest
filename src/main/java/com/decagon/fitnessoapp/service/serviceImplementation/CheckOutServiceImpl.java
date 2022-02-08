package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.CheckOutRequest;
import com.decagon.fitnessoapp.dto.CheckOutResponse;
import com.decagon.fitnessoapp.dto.transactionDto.TransactionResponseDTO;
import com.decagon.fitnessoapp.model.product.CheckOut;
import com.decagon.fitnessoapp.model.product.CouponCode;
import com.decagon.fitnessoapp.model.product.Cart;
import com.decagon.fitnessoapp.model.product.ORDER_STATUS;
import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.PaymentCard;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.*;
import com.decagon.fitnessoapp.service.CheckOutService;
import com.decagon.fitnessoapp.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckOutRepository checkOutRepository;
    private final PersonRepository personRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final AddressRepository addressRepository;
    private final CouponCodeRepository couponCodeRepository;
    private final ModelMapper modelMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final TransactionService transactionService;


    @Override
    public CheckOutResponse save(CheckOutRequest checkOutRequest) {
        CheckOut checkOut = new CheckOut();
        Address billingAddress = new Address();
        Address shippingAddress = new Address();
        PaymentCard paymentCard = new PaymentCard();
        Optional<CouponCode> couponCode = couponCodeRepository.
                findByCouponCode(checkOutRequest.getDiscountRequest().getCouponCode());
        Optional<Person> personExists = personRepository.findByEmail(checkOutRequest.getEmail());
        Optional<Cart> shoppingItemExist = shoppingCartRepository.findById(checkOutRequest.getShoppingCartId());
        if(personExists.isPresent()){
            if(shoppingItemExist.isPresent()){
                if(shoppingItemExist.get().getCartReference() == null) {
                    checkOut.setPerson(personExists.get());
                    billingAddress.setPerson(personExists.get());
                    shippingAddress.setPerson(personExists.get());
                    paymentCard.setPerson(personExists.get());
                    paymentCard.setAccountName(personExists.get().getFirstName());
                    modelMapper.map(checkOutRequest.getPaymentRequest(), paymentCard);
                    modelMapper.map(checkOutRequest.getBillingAddress(), billingAddress);
                    modelMapper.map(checkOutRequest.getShippingAddress(), shippingAddress);
                    paymentCardRepository.save(paymentCard);
                    addressRepository.save(billingAddress);
                    addressRepository.save(shippingAddress);
                    shoppingItemExist.get().setCartReference(RandomString.make(10));
                    shoppingCartRepository.save(shoppingItemExist.get());
                    checkOut.setTotalPrice(checkOutRequest.getOrderSummary().getTotal());
                    checkOut.setBillingAddress(billingAddress);
                    checkOut.setShippingAddress(shippingAddress);
                    checkOut.setShoppingCart(shoppingItemExist.get());
                    checkOut.setShippingMethod(checkOutRequest.getShippingMethod());
                    if (couponCode.isPresent() && couponCode.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                        final BigDecimal total_cost = checkOutRequest.getOrderSummary().getTotal();
                        checkOut.setTotalPrice(BigDecimal.
                                valueOf(total_cost.floatValue() * couponCode.get().getDiscountPercent()));
                        checkOut.setCouponCode(checkOutRequest.getDiscountRequest().getCouponCode());
                    }
                    checkOut.setPaymentCard(paymentCard);
                    final String referenceNumber = "fitnesso-app-" + RandomString.make(16);
                    checkOut.setOrderStatus(ORDER_STATUS.PENDING);
                    checkOut.setReferenceNumber(referenceNumber);
                    checkOutRepository.save(checkOut);
                    TransactionResponseDTO response = transactionService.initializeTransaction(
                            personExists.get().getEmail(),
                            checkOut.getTotalPrice(), referenceNumber, checkOutRequest.getPaymentRequest().getCardNumber());
                    return CheckOutResponse.builder()
                            .message("Complete your Payment").link(response.getData().getAuthorization_url())
                            .timestamp(LocalDateTime.now()).build();
                }


                Optional<CheckOut> checkOutResend = checkOutRepository.
                        findCheckOutByShoppingCartId(checkOutRequest.getShoppingCartId());
                if(checkOutResend.isPresent() && checkOutResend.get().getOrderStatus().equals(ORDER_STATUS.PENDING)){
                    TransactionResponseDTO response = transactionService.initializeTransaction(
                            personExists.get().getEmail(),
                            checkOutResend.get().getTotalPrice(),
                            checkOutResend.get().getReferenceNumber(),
                            checkOutResend.get().getPaymentCard().getCardNumber());
                    return CheckOutResponse.builder()
                            .message("Complete your Payment").link(response.getData().getAuthorization_url())
                            .timestamp(LocalDateTime.now()).build();
                }
                else if (checkOutResend.isPresent() && checkOutResend.get().getOrderStatus().equals(ORDER_STATUS.COMPLETED)){
                    return CheckOutResponse.builder()
                            .message("Shopping Items have been fully paid").timestamp(LocalDateTime.now()).build();
                }
                return CheckOutResponse.builder()
                        .message("Error! finding the Cart").timestamp(LocalDateTime.now()).build();
            }

            return CheckOutResponse.builder().message("Shopping cart is empty").timestamp(LocalDateTime.now()).build();

        }
        return CheckOutResponse.builder().message("User not Registered").timestamp(LocalDateTime.now()).build();
    }



    public CheckOut findByReferenceNumber(String referenceNumber){
        Optional<CheckOut> checkOut = checkOutRepository.findByReferenceNumber(referenceNumber);
        return checkOut.orElse(null);
    }



    public CheckOut updateCheckOut(CheckOut checkOut){
        return checkOutRepository.save(checkOut);
    }
}
