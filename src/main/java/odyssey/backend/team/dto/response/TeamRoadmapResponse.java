package odyssey.backend.team.dto.response;

import odyssey.backend.roadmap.domain.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TeamRoadmapResponse(
        Long id,
        String title,
        String description,
        List<String> categories,
        String thumbnailUrl,
        LocalDate lastModifiedAt,
        LocalDateTime lastAccessedAt,
        boolean isFavorite,
        String location,
        Long teamId,
        String teamName
) {
    public static TeamRoadmapResponse from(Roadmap roadmap, Long teamId, String teamName) {
        return new TeamRoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                roadmap.getImageUrl(),
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                teamName,
                teamId,
                teamName
        );
    }
}