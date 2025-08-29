package odyssey.backend.websocket.dto.node;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeDeletedNotification {

    @NotNull
    private Long nodeId;

    @NotNull
    private Long roadmapId;
}
