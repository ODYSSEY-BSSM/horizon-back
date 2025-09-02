package odyssey.backend.directory.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.exception.ErrorProperty;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DirectoryExceptionProperty implements ErrorProperty {
    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "디렉토리를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
