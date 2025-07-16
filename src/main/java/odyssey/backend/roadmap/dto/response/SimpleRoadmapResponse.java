package odyssey.backend.roadmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

@Getter
@AllArgsConstructor
public class SimpleRoadmapResponse {

    private final Long id;

    private final String title;

    @Builder
    public SimpleRoadmapResponse(Roadmap roadmap) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
    }
}
