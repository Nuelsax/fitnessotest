package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
}
