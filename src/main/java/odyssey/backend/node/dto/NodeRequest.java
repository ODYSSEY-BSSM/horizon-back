package odyssey.backend.node.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "필수값입니다.")
    private int height;

    @NotBlank(message = "필수값입니다.")
    private int width;

    @NotBlank(message = "필수값입니다.")
    private NodeType type;

    @NotBlank(message = "필수값입니다.")
    private int x;

    @NotBlank(message = "필수값입니다.")
    private int y;

    @NotBlank(message = "필수값입니다.")
    private Long parentId;
}
