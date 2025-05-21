package odyssey.backend.node.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.node.domain.NodeType;

@Getter
@AllArgsConstructor
public class NodeRequest {

    @NotBlank(message = "필수값입니다.")
    @Size(max = 64)
    private String title;

    @NotBlank(message = "필수값입니다.")
    @Size(max = 1500)
    private String description;

    @NotNull(message = "필수값입니다.")
    private int height;

    @NotNull(message = "필수값입니다.")
    private int width;

    @NotNull(message = "필수값입니다.")
    private NodeType type;

    @NotNull(message = "필수값입니다.")
    private int x;

    @NotNull(message = "필수값입니다.")
    private int y;

    @NotBlank(message = "필수값입니다.")
    private String category;

    private Long parentNodeId;
}
