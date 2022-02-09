package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.TestCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCartRepository extends JpaRepository<TestCart, Long> {
    Optional<TestCart> findByUniqueCartId(String uniqueId);
    List<TestCart> findAllByUniqueCartId(String uniqueId);
}
