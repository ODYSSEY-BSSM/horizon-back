package odyssey.backend.global.exception;

import odyssey.backend.directory.exception.DirectoryNotFoundException;
import odyssey.backend.node.exception.NodeNotFoundException;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return buildResponse(ExceptionCode.INVALID_REQUEST, errorMessage);
    }

    @ExceptionHandler(DirectoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDirectoryNotFoundException(DirectoryNotFoundException e) {
        return buildResponse(ExceptionCode.DIRECTORY_NOT_FOUND);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getStatus())
                .body(new ExceptionResponse(exceptionCode.getStatus().value(), exceptionCode.getMessage()));
    }

    private ResponseEntity<ExceptionResponse> buildResponse(ExceptionCode exceptionCode, String customMessage) {
        return ResponseEntity
                .status(exceptionCode.getStatus())
                .body(new ExceptionResponse(exceptionCode.getStatus().value(), customMessage));
    }

}
