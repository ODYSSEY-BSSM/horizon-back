package odyssey.backend.roadmap.dto.response;

import odyssey.backend.roadmap.domain.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RoadmapResponse(
        Long id,
        String title,
        String description,
        List<String> categories,
        String thumbnailUrl,
        LocalDate lastModifiedAt,
        LocalDateTime lastAccessedAt,
        boolean isFavorite,
        String location
) {
    public static RoadmapResponse from(Roadmap roadmap, String thumbnailUrl) {
        return new RoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                thumbnailUrl,
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                "내 로드맵"
        );
    }
}
