package odyssey.backend.websocket.dto.node;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.node.domain.NodeType;

@Getter
@AllArgsConstructor
public class NodeCreatedNotification {

    @NotNull
    private Long id;

    @NotNull
    private Long roadmapId;

    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 1500)
    private String description;

    @NotNull
    private Integer height;

    @NotNull
    private Integer width;

    @NotNull
    private NodeType type;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotBlank
    private String category;

    private Long parentNodeId;


}
