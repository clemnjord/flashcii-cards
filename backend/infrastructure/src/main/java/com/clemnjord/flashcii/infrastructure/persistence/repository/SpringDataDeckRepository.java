package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataDeckRepository extends JpaRepository<DeckEntity, UUID> {
    boolean existsByName(String name);

    Optional<DeckEntity> findByName(String name);
}
