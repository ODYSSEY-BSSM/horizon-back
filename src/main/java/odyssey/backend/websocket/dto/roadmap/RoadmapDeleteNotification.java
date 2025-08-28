package odyssey.backend.websocket.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadmapDeleteNotification {
    private Long roadmapId;
}