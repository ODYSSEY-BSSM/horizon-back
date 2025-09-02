package odyssey.backend.user.exception;

import odyssey.backend.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(){
        super(UserExceptionProperty.USER_NOT_FOUND);
    }
}
