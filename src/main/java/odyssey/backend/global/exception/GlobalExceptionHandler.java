package odyssey.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RoadmapNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoadmapNotFoundException(RoadmapNotFoundException e) {
        return buildResponse(ExecptionCode.ROADMAP_NOT_FOUND);
    }

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNodeNotFoundException(NodeNotFoundException e) {
        return buildResponse(ExecptionCode.NODE_NOT_FOUND);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(ExecptionCode execptionCode) {
        return ResponseEntity
                .status(execptionCode.getStatus())
                .body(new ExceptionResponse(execptionCode.getStatus().value(), execptionCode.getMessage()));
    }
}
