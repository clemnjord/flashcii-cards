package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.QuizEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface JpaQuizDao extends JpaRepository<QuizEntity, UUID> {

    Optional<QuizEntity> findById(@Param("id") UUID id);
}
