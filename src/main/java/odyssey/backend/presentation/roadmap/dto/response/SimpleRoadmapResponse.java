package odyssey.backend.presentation.roadmap.dto.response;

import odyssey.backend.domain.roadmap.Roadmap;

public record SimpleRoadmapResponse(Long id, String title) {
    public static SimpleRoadmapResponse from(Roadmap roadmap) {
        return new SimpleRoadmapResponse(roadmap.getId(), roadmap.getTitle());
    }
}
