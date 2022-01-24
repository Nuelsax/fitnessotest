package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByTokenCode(String tokenCode);
}
