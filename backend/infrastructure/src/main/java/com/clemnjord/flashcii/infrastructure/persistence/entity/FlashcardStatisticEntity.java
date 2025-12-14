package com.clemnjord.flashcii.infrastructure.persistence.entity;

import io.github.openspacedrepetition.State;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "flashcard_statistics")
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardStatisticEntity {

    @EmbeddedId
    private FlashcardEntityId id;

    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private Integer step;

    @Column
    private Double stability;

    @Column
    private Double difficulty;

    @Column(nullable = false)
    private Instant due;

    @Column
    private Instant lastReview;
}
