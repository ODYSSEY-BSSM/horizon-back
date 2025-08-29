package odyssey.backend.websocket.dto.node;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeMoveMessage {

    @NotNull
    private Long id;

    @NotNull
    private Long roadmapId;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    private Long parentNodeId;
}
