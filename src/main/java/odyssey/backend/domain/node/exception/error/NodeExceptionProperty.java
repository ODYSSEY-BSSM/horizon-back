package odyssey.backend.domain.node.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.exception.ErrorProperty;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NodeExceptionProperty implements ErrorProperty {
    NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 노드를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
