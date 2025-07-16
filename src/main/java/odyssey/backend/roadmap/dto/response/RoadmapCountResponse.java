package odyssey.backend.roadmap.dto.response;

import lombok.Getter;

@Getter
public class RoadmapCountResponse {

    private final Long count;

    public RoadmapCountResponse(Long count) {
        this.count = count;
    }
}
