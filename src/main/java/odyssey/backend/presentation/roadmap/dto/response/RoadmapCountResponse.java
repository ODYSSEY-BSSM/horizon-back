package odyssey.backend.presentation.roadmap.dto.response;

public record RoadmapCountResponse(Long count) {
    public static RoadmapCountResponse from(Long count) {
        return new RoadmapCountResponse(count);
    }
}
