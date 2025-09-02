package odyssey.backend.global.exception;

import org.apache.http.HttpStatus;

public interface ErrorProperty {
    HttpStatus getStatus();
    String getMessage();
    String name();
}
