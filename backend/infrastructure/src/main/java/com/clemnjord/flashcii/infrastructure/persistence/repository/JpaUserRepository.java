package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.UserMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements IUserRepository {
    private final JpaUserDao jpaUserDao;
    private final UserMapper userMapper;

    public JpaUserRepository(JpaUserDao jpaUserDao, UserMapper userMapper) {
        this.jpaUserDao = jpaUserDao;
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        jpaUserDao.save(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserDao.existsByUsername(username);
    }

    @Override
    public Optional<User> findById(UserId user) {
        return jpaUserDao.findByUuid(user.uuid()).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserDao.findByUsername(username).map(userMapper::toDomain);
    }
}
