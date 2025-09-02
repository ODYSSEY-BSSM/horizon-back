package odyssey.backend.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonResponse {
    String code;
    String message;

    public static <T> SingleCommonResponse<T> ok(T data) {
        return new SingleCommonResponse<>(
                "OK",
                "ok",
                data
        );
    }

    public static <T> ListCommonResponse<T> ok(List<T> dataList) {
        return new ListCommonResponse<>(
                "OK",
                "ok",
                dataList
        );
    }


}
