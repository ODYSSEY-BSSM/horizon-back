package odyssey.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    ROADMAP_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 로드맵입니다."),
    NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 노드입니다."),
    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 디렉토e입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
