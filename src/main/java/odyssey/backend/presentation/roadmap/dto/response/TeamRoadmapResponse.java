package odyssey.backend.presentation.roadmap.dto.response;

import odyssey.backend.domain.roadmap.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TeamRoadmapResponse(
        Long id,
        String title,
        String description,
        List<String> categories,
        LocalDate lastModifiedAt,
        LocalDateTime lastAccessedAt,
        boolean isFavorite,
        Long uuid,
        String color,
        String icon,
        Long teamId,
        String teamName,
        Integer progress
) {
    public static TeamRoadmapResponse from(Roadmap roadmap, Long uuid){
        return new TeamRoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                uuid,
                roadmap.getColorCode(),
                roadmap.getIconCode(),
                roadmap.getTeamId(),
                roadmap.getTeamName(),
                roadmap.getProgress()
        );
    }
}
