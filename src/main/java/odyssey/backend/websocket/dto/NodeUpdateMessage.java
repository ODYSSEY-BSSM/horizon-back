package odyssey.backend.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeUpdateMessage {

    private String action;

    private Long nodeId;

    private Long roadmapId;

    private Map<String, Object> payload;

    private Long userId;

    private String userName;
}
