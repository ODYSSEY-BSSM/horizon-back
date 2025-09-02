package odyssey.backend.domain.auth.exception;

import odyssey.backend.shared.exception.GlobalException;

public class InvalidPasswordException extends GlobalException {
    public InvalidPasswordException(){
        super(UserExceptionProperty.INVALID_PASSWORD);
    }
}
