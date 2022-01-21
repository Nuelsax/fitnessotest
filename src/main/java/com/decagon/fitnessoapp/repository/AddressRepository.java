package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}