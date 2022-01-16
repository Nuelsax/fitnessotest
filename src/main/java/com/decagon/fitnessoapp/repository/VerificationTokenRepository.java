package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
