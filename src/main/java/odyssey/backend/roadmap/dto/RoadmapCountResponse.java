package odyssey.backend.roadmap.dto;

import lombok.Getter;

@Getter
public class RoadmapCountResponse {

    private Long count;

    public RoadmapCountResponse(Long count) {
        this.count = count;
    }
}
