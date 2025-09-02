package odyssey.backend.directory.exception;

import odyssey.backend.global.exception.GlobalException;

public class DirectoryNotFoundException extends GlobalException {
    public DirectoryNotFoundException() {
        super(DirectoryExceptionProperty.DIRECTORY_NOT_FOUND);
    }
}
