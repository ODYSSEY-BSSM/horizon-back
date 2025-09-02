package odyssey.backend.domain.auth.exception;

import odyssey.backend.shared.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(){
        super(UserExceptionProperty.USER_NOT_FOUND);
    }
}
