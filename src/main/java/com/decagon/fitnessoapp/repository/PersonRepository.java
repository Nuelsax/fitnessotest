package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUserName(String userName);
    Optional<Person> findByEmail(String email);
    Optional<Person> findByResetPasswordToken(String token);
}
