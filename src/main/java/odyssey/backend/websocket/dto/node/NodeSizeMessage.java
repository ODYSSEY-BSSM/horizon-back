package odyssey.backend.websocket.dto.node;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NodeSizeMessage {

    @NotNull
    private Long id;

    @NotNull
    private Long roadmapId;

    @NotNull
    private Integer height;

    @NotNull
    private Integer width;
}
