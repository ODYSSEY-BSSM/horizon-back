package odyssey.backend.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

@Getter
@AllArgsConstructor
public class RoadmapResponse {

    private Long id;

    private String title;

    private String description;

    public RoadmapResponse(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.description = roadmap.getDescription();
    }
}
