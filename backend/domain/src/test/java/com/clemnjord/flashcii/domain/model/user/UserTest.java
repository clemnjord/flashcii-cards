package com.clemnjord.flashcii.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void createNewUserWhenInputIsValid() {
        User user = User.createNew(new Username("testuser"));

        assertThat(user).isNotNull();
        assertThat(user.userId()).isNotNull();
        assertThat(user.username().value()).hasToString("testuser");
    }

    @Test
    void throwExceptionWhenUserIdIsNull() {
        UserId userId = UserId.generate();
        Assertions.assertThatThrownBy(() -> new User(userId, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Username cannot be null");
    }

    @Test
    void userEquality() {
        EqualsVerifier.forClass(User.class).suppress(Warning.NULL_FIELDS).verify();
    }
}
