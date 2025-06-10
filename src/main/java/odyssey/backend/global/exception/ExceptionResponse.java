package odyssey.backend.global.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int status;
    private String message;

    public ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}