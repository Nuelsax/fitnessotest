package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.CheckOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {

}
