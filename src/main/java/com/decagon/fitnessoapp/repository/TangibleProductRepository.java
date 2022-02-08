package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.TangibleProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TangibleProductRepository extends JpaRepository<TangibleProduct, Long> {

    Optional<TangibleProduct> findTangibleProductByProductName(String productName);

    @Query("SELECT t FROM TangibleProduct t WHERE CONCAT(t.category, t.productName, t.description) LIKE %?1%")
    List<TangibleProduct> findTangibleProductByCategoryOrProductNameOrByDescription(String freeText);
}
