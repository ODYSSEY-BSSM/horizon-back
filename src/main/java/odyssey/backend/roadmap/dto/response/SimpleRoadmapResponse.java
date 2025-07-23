package odyssey.backend.roadmap.dto.response;

import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

@Getter
public class SimpleRoadmapResponse {

    private final Long id;

    private final String title;

    public static SimpleRoadmapResponse from(Roadmap roadmap) {
        return new SimpleRoadmapResponse(roadmap.getId(), roadmap.getTitle());
    }

    SimpleRoadmapResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
