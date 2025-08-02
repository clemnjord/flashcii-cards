package com.clemnjord.flashcii.infrastructure.persistence.mapper;

import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getUuid()),
                new Username(entity.getUsername())
        );
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();

        if (user.userId() != null && user.userId().uuid() != null) {
            entity.setUuid(user.userId().uuid());
        }

        entity.setUsername(user.username().value());

        return entity;
    }
}