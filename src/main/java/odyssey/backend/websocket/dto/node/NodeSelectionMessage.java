package odyssey.backend.websocket.dto.node;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeSelectionMessage {

    @NotNull
    private Long id;

    @NotNull
    private Long roadmapId;

    private Boolean isSelected = false;
}
