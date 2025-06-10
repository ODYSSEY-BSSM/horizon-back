package odyssey.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private int status;

    private String message;

    public ExceptionResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
