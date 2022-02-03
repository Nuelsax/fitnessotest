package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.dto.FavouriteResponse;
import com.decagon.fitnessoapp.model.user.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    Optional<Favourite> findFavouriteByProductId(Long id);
    @Transactional
    void deleteFavouriteByProductId(Long id);
    List<Favourite> findFavouritesByPersonId(Long id);
}
