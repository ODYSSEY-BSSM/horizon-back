package odyssey.backend.node.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeResizeRequest {
    
    @NotNull
    @Positive
    private Integer width;
    
    @NotNull
    @Positive
    private Integer height;
}