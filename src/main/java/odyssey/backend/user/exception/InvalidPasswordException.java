package odyssey.backend.user.exception;

import odyssey.backend.global.exception.GlobalException;

public class InvalidPasswordException extends GlobalException {
    public InvalidPasswordException(){
        super(UserExceptionProperty.INVALID_PASSWORD);
    }
}
