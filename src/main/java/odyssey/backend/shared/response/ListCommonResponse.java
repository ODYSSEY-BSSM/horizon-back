package odyssey.backend.shared.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ListCommonResponse<T> extends CommonResponse {
    List<T> data;

    public ListCommonResponse(String code, String message, List<T> data) {
        super(code, message);
        this.data = data;
    }
}