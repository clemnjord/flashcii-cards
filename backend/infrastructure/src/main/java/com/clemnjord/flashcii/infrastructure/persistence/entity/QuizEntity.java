package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quizzes")
@AllArgsConstructor
@NoArgsConstructor
public class QuizEntity {

    @Id
    @Column(name = "quiz_id", nullable = false)
    private UUID quizId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner; // Natural key part

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "quiz_flashcards",
            joinColumns = @JoinColumn(name = "quiz_id", referencedColumnName = "quiz_id"),
            inverseJoinColumns = {
                @JoinColumn(name = "flashcard_id", referencedColumnName = "flashcard_id"),
                @JoinColumn(name = "owner_id", referencedColumnName = "owner_id")
            })
    private List<FlashcardEntity> flashcards;

    @Column(nullable = false)
    private int currentQuestionIndex;
}
