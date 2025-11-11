package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;

public interface IAuthorizationService {
    boolean canManageResourceFor(User currentUser, UserId resourceOwnerId);
}
