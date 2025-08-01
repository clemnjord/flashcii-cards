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

    public Deck toDomain(DeckEntity entity) {
        Set<FlashcardId> flashcardIds = entity.getFlashcards().stream().map(e -> new FlashcardId(e.getUUID())).collect(Collectors.toSet());

        return new Deck(
                new DeckId(entity.getUUID()),
                entity.getName(),
                entity.getDescription(),
                new UserId(entity.getOwner().getUuid()),
                flashcardIds);
    }

    public DeckEntity toEntity(Deck deck, UserEntity owner) {
        DeckEntity entity = new DeckEntity();

        // Only set ID if it exists (for updates), let JPA generate it for new entities
        if (deck.getDeckId() != null && deck.getDeckId().uuid() != null) {
            entity.setId(deck.getDeckId().uuid());
        }

        entity.setName(deck.getName());
        entity.setDescription(deck.getDescription());
        entity.setOwner(owner);

        // Or user `EntityManager` to create lazy reference without immediate fetch?
//        UserEntity ownerRef = entityManager.getReference(UserEntity.class, deck.getOwnerId().uuid());
//        entity.setOwner(ownerRef);


        return entity;
    }
}