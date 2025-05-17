package com.clemnjord.flashcii.user.domain.model;

import com.clemnjord.flashcii.shared.kernel.UserId;
import lombok.Builder;

@Builder
public record User(UserId userId,
                   String username) {
}
