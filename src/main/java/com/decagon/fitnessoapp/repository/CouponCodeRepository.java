package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCodeRepository extends JpaRepository<CouponCode, Long> {
}
