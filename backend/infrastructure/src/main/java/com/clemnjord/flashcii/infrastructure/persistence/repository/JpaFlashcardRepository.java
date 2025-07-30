package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFlashcardRepository implements IFlashcardRepository {
    private final SpringDataCardRepository springRepository;

    public JpaFlashcardRepository(SpringDataCardRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Optional<Flashcard> findById(FlashcardId flashcardId) {
        return springRepository
                .findById(flashcardId.uuid())
                .map(
                        x ->
                                new Flashcard(
                                        new FlashcardId(x.getUUID()),
                                        new Question(x.getQuestion()),
                                        new Answer(x.getAnswer())));
    }

    @Override
    public boolean existsByQuestionAndDeckId(Question question, DeckId deckId) {
        return springRepository.existsByQuestionAndDeck_Uuid(question.value(), deckId.uuid());
    }

    @Override
    public Flashcard save(Flashcard flashcard) {
        FlashcardEntity flashcardEntity = new FlashcardEntity();
        flashcardEntity.setAnswer(flashcard.answer().value());
        flashcardEntity.setQuestion(flashcard.question().value());

        FlashcardEntity saved = springRepository.save(flashcardEntity);

        return new Flashcard(
                new FlashcardId(saved.getUUID()),
                new Question(saved.getQuestion()),
                new Answer(saved.getAnswer()));
    }
}
