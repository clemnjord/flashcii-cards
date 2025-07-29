package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.ICardRepository;
import com.clemnjord.flashcii.domain.model.*;
import com.clemnjord.flashcii.infrastructure.persistence.entity.CardEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaCardRepository implements ICardRepository {
    private final SpringDataCardRepository springRepository;

    public JpaCardRepository(SpringDataCardRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Optional<Card> findById(CardId cardId) {
        return springRepository
                .findById(cardId.uuid())
                .map(
                        x ->
                                new Card(
                                        new CardId(x.getUUID()),
                                        new Question(x.getQuestion()),
                                        new Answer(x.getAnswer())));
    }

    @Override
    public boolean existsByQuestionAndCollectionId(String question, CollectionId collectionId) {
        return springRepository.existsByQuestionAndCollection_Uuid(question, collectionId.uuid());
    }

    @Override
    public Card save(Card card) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setAnswer(card.answer().value());
        cardEntity.setQuestion(card.question().value());

        CardEntity saved = springRepository.save(cardEntity);

        return new Card(
                new CardId(saved.getUUID()),
                new Question(saved.getQuestion()),
                new Answer(saved.getAnswer()));
    }
}
