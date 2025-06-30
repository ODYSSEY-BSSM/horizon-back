package odyssey.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RoadmapNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoadmapNotFoundException(RoadmapNotFoundException e) {
        return buildResponse(ExceptionCode.ROADMAP_NOT_FOUND);
    }

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNodeNotFoundException(NodeNotFoundException e) {
        return buildResponse(ExceptionCode.NODE_NOT_FOUND);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getStatus())
                .body(new ExceptionResponse(exceptionCode.getStatus().value(), exceptionCode.getMessage()));
    }
}
