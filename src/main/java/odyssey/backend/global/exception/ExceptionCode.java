package odyssey.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    ROADMAP_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 로드맵입니다."),
    NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 노드입니다."),
    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 디렉토리입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "올바른 이미지 형식이 아닙니다."),
    INVALID_ACTION(HttpStatus.BAD_REQUEST, "지원하지 않는 액션입니다."),
    IMAGE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
