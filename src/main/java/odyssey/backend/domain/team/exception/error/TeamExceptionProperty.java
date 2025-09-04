package odyssey.backend.domain.team.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.exception.ErrorProperty;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamExceptionProperty implements ErrorProperty {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀이 존재하지 않습니다");

    private final HttpStatus status;
    private final String message;
}
