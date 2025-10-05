package com.clemnjord.flashcii.application.usecase.user;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.port.output.IAuthorizationService;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;

@ApplicationService
public class AuthorizationUseCase implements IAuthorizationService {

    @Override
    public boolean canManageResourceFor(User currentUser, UserId resourceOwnerId) {
        // Admin can manage anything
//        if (currentUser.role() == Role.ADMIN) {
//            return true;
//        }

        // User can only manage their own resources
        return currentUser.userId().equals(resourceOwnerId);
    }
}