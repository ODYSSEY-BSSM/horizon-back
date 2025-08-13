package odyssey.backend.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import odyssey.backend.websocket.domain.RoadmapActionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapCrudMessage {

    private RoadmapActionType action;

    private Long roadmapId;

    private String title;

    private String description;

    private List<String> categories;

    private String imageUrl;

    private boolean isFavorite;

    private LocalDate lastModifiedAt;

    private LocalDateTime lastAccessedAt;

    private Long userId;

    private String userName;
}
