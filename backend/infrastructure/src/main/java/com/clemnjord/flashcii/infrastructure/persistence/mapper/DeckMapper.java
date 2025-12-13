package com.clemnjord.flashcii.infrastructure.persistence.mapper;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DeckMapper {

    public Deck toDomainWithFlashcards(DeckEntity entity) {
        Set<FlashcardId> flashcardIds = entity.getFlashcards().stream()
                .map(e -> new FlashcardId(e.getId().getFlashcardId()))
                .collect(Collectors.toSet());

        return Deck.restore(
                new DeckId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                new UserId(entity.getOwner().getId()),
                flashcardIds);
    }

    public Deck toDomainWithoutFlashcards(DeckEntity entity) {
        return Deck.restore(
                new DeckId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                new UserId(entity.getOwner().getId()),
                Set.of());
    }

    public DeckEntity toEntity(Deck deck, UserEntity owner) {
        DeckEntity entity = new DeckEntity();

        entity.setId(deck.deckId().uuid());
        entity.setName(deck.name());
        entity.setDescription(deck.description());
        entity.setOwner(owner);

        return entity;
    }
}
