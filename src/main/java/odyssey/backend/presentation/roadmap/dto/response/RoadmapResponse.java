package odyssey.backend.presentation.roadmap.dto.response;

import odyssey.backend.domain.roadmap.Roadmap;

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
        String location,
        Long uuid,
        Long teamId
) {
    public static RoadmapResponse from(Roadmap roadmap, Long uuid) {
        return new RoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                roadmap.getImageUrl(),
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                "내 로드맵",
                uuid,
                roadmap.getTeamId()
        );
    }
}
