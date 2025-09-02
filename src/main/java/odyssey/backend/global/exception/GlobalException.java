package odyssey.backend.global.exception;

import lombok.Getter;

@Getter
public abstract class GlobalException extends RuntimeException {
    private final ErrorProperty errorProperty;

    public GlobalException(ErrorProperty errorProperty) {
        super(errorProperty.getMessage());
        this.errorProperty = errorProperty;
    }

}
