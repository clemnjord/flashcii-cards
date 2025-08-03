package com.clemnjord.flashcii.infrastructure.persistence.mapper;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeckMapper {

    public Deck toDomainWithFlashcards(DeckEntity entity) {
        Set<FlashcardId> flashcardIds = entity.getFlashcards().stream().map(e -> new FlashcardId(e.getUUID())).collect(Collectors.toSet());

        return Deck.restore(
                new DeckId(entity.getUUID()),
                entity.getName(),
                entity.getDescription(),
                new UserId(entity.getOwner().getUuid()),
                flashcardIds);
    }

    public Deck toDomainWithoutFlashcards(DeckEntity entity) {
        return Deck.restore(
                new DeckId(entity.getUUID()),
                entity.getName(),
                entity.getDescription(),
                new UserId(entity.getOwner().getUuid()),
                Set.of());
    }


    public DeckEntity toEntity(Deck deck, UserEntity owner) {
        DeckEntity entity = new DeckEntity();

        // Only set ID if it exists (for updates), let JPA generate it for new entities
        if (deck.deckId() != null && deck.deckId().uuid() != null) {
            entity.setId(deck.deckId().uuid());
        }

        entity.setName(deck.name());
        entity.setDescription(deck.description());
        entity.setOwner(owner);

        return entity;
    }
}