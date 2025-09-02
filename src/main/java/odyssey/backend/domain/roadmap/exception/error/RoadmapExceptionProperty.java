package odyssey.backend.domain.roadmap.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.exception.ErrorProperty;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoadmapExceptionProperty implements ErrorProperty {
    ROADMAP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 로드맵을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
