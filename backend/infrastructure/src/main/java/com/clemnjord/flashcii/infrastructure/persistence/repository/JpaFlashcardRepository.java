package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.FlashcardMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFlashcardRepository implements IFlashcardRepository {
    private final JpaFlashcardDao springRepository;
    private final JpaDeckDao jpaDeckDao;
    private final FlashcardMapper flashcardMapper;

    public JpaFlashcardRepository(JpaFlashcardDao springRepository, JpaDeckDao jpaDeckDao, FlashcardMapper flashcardMapper) {
        this.springRepository = springRepository;
        this.jpaDeckDao = jpaDeckDao;
        this.flashcardMapper = flashcardMapper;
    }

    @Override
    public Optional<Flashcard> findById(FlashcardId flashcardId) {
        return springRepository
                .findById(flashcardId.uuid())
                .map(flashcardMapper::toDomain);
    }

    @Override
    public boolean existsByQuestionAndDeckId(Question question, DeckId deckId) {
        return springRepository.existsByQuestionAndDeck_Uuid(question.value(), deckId.uuid());
    }

    @Override
    public void save(Flashcard flashcard, DeckId deckId) {
        Optional<DeckEntity> deckEntity = jpaDeckDao.findByUuid(deckId.uuid());

        if (deckEntity.isPresent()) {

            FlashcardEntity flashcardEntity = flashcardMapper.toEntity(flashcard, deckEntity.get());
            springRepository.save(flashcardEntity);
        }
    }
}
