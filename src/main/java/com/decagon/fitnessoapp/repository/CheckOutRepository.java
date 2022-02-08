package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.CheckOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {
    Optional<CheckOut> findByReferenceNumber(String referenceNumber);
    Optional<CheckOut> findCheckOutByShoppingCartId(Long cartId);

}
