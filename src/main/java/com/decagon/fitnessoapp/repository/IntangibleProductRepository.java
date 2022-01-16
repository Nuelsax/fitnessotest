package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntangibleProductRepository extends JpaRepository<IntangibleProduct, Long> {
}
