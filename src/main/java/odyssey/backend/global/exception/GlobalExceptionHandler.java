package odyssey.backend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoadmapNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoadmapNotFoundException(RoadmapNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, "존재하지 않는 로드맵입니다.");
    }

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNodeNotFoundException(NodeNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, "존재하지 않는 노드입니다.");
    }

    private ResponseEntity<ExceptionResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new ExceptionResponse(status.value(), message), status);
    }
}
