package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.DeckMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaDeckRepository implements IDeckRepository {
    private final JpaDeckDao jpaDeckDao;
    private final JpaUserDao jpaUserDao;
    private final DeckMapper deckMapper;


    public JpaDeckRepository(JpaDeckDao jpaDeckDao, JpaUserDao jpaUserDao, DeckMapper deckMapper) {
        this.jpaDeckDao = jpaDeckDao;
        this.jpaUserDao = jpaUserDao;
        this.deckMapper = deckMapper;
    }

    @Override
    public Deck save(Deck deck) {
        UserEntity owner = jpaUserDao.findById(deck.getOwnerId().uuid())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + deck.getOwnerId()));

        DeckEntity deckEntity = deckMapper.toEntity(deck, owner);

        DeckEntity saved = jpaDeckDao.save(deckEntity);

        return deckMapper.toDomain(saved);
    }

    @Override
    public boolean existsByName(String name) {

        return jpaDeckDao.existsByName(name);
    }

    @Override
    public boolean existsById(DeckId id) {
        return jpaDeckDao.existsById(id.uuid());
    }

    @Override
    public Optional<Deck> findByName(String name) {
        return jpaDeckDao.findByName(name).map(x -> new Deck(new DeckId(x.getUUID()), x.getName(), x.getDescription(), new UserId(x.getOwner().getUuid()), new HashSet<>()));
    }

    @Override
    public Optional<Deck> findById(DeckId id) {
        return jpaDeckDao.findById(id.uuid()).map(x -> new Deck(new DeckId(x.getUUID()), x.getName(), x.getDescription(), new UserId(x.getOwner().getUuid()), new HashSet<>()));
    }

    @Override
    public List<Deck> findAllByOwnerIdAndNameContainsIgnoreCase(UserId ownerId, String nameFilter) {
        return jpaDeckDao.findAllByOwner_UuidAndNameContainsIgnoreCase(ownerId.uuid(), nameFilter).stream()
                .map(deckMapper::toDomain)
                .toList();
    }
}
