package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.TangibleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TangibleProductRepository extends JpaRepository<TangibleProduct, Long> {
}
