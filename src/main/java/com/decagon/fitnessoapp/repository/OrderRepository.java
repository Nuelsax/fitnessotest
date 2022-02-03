package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderByPerson_Id(Long personId);
}
