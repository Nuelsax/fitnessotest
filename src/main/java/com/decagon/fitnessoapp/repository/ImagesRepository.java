package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.product.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
    Optional<List<Image>> findByProductName(String name);
    Optional<List<Image>> findAllByProductName(String name);
}
