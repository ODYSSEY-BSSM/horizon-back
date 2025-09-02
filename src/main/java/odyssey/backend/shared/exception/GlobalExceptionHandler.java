package odyssey.backend.shared.exception;

import lombok.extern.slf4j.Slf4j;
import odyssey.backend.shared.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage())
        );

        logHandledException(e);

        return ResponseEntity
                .status(GlobalErrorProperty.BAD_REQUEST.getStatus())
                .body(new ErrorResponse(GlobalErrorProperty.BAD_REQUEST, errorMap));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e) {
        logHandledException(e);

        return ResponseEntity
                .status(e.getErrorProperty().getStatus())
                .body(new ErrorResponse(e.getErrorProperty(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorProperty.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ErrorResponse(GlobalErrorProperty.INTERNAL_SERVER_ERROR));
    }

    private void logHandledException(Exception e) {
        log.warn("Resolved [{}: {}]", e.getClass().getName(), e.getMessage());
    }

}