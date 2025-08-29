package odyssey.backend.websocket.dto.cursor;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorMessage {

    @NotNull
    private Long roadmapId;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;
}
