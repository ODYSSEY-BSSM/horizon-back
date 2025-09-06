package odyssey.backend.presentation.websocket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorMessage {

    @NotNull(message = "roadmapId는 필수입니다.")
    private Long roadmapId;

    @NotNull(message = "x좌표는 필수입니다.")
    private Integer x;

    @NotNull(message = "y좌표는 필수입니다.")
    private Integer y;
}
