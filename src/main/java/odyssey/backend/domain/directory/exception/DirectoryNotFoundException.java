package odyssey.backend.domain.directory.exception;

import odyssey.backend.shared.exception.GlobalException;

public class DirectoryNotFoundException extends GlobalException {
    public DirectoryNotFoundException() {
        super(DirectoryExceptionProperty.DIRECTORY_NOT_FOUND);
    }
}
