package odyssey.backend.websocket.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapDeleteNotification {
    private Long roadmapId;
}