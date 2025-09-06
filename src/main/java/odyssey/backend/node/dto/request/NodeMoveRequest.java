package odyssey.backend.node.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeMoveRequest {
    
    @NotNull
    private Integer x;
    
    @NotNull
    private Integer y;
    
    private Long parentNodeId;
}