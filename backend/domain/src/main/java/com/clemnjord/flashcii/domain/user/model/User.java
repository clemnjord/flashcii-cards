package com.clemnjord.flashcii.domain.user.model;

import lombok.Builder;

@Builder
public record User(UserId userId,
                   String username) {
}
