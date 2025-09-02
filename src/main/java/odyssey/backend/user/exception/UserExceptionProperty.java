package odyssey.backend.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.exception.ErrorProperty;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionProperty implements ErrorProperty {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.");

    private final HttpStatus status;
    private final String message;
}
