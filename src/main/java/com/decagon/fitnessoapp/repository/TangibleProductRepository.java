package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.TangibleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TangibleProductRepository extends JpaRepository<TangibleProduct, Long> {

    Optional<TangibleProduct> findTangibleProductByProductName(String productName);
}
