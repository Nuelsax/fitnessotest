package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
