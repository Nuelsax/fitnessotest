package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByPerson_Id(Long id);
}
