package odyssey.backend.roadmap.dto;

import lombok.Builder;
import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

@Getter
public class SimpleRoadmapResponse {

    private final Long id;

    private final String title;

    @Builder
    public SimpleRoadmapResponse(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
    }
}
