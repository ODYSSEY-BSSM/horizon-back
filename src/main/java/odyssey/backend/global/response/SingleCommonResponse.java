package odyssey.backend.global.response;
import lombok.Getter;

@Getter
public class SingleCommonResponse<T> extends CommonResponse {
    T data;

    public SingleCommonResponse(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }
}

