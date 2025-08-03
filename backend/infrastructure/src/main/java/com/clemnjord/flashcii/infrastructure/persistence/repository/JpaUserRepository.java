package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements IUserRepository {
    private final JpaUserDao springRepository;
    private final UserMapper userMapper;

    public JpaUserRepository(JpaUserDao springRepository, UserMapper userMapper) {
        this.springRepository = springRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        springRepository.save(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findById(UserId user) {
        return springRepository.findByUuid(user.uuid()).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springRepository.findByUsername(username).map(userMapper::toDomain);
    }
}
