package odyssey.backend.global.exception;

import org.apache.http.HttpStatus;

public interface ErrorProperty {
    HttpStatus getHttpStatus();
    String getMessage();
    String name();
}
