package odyssey.backend.presentation.roadmap.dto.response;

import odyssey.backend.domain.roadmap.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PersonalRoadmapResponse(
        Long id,
        String title,
        String description,
        List<String> categories,
        String thumbnailUrl,
        LocalDate lastModifiedAt,
        LocalDateTime lastAccessedAt,
        boolean isFavorite,
        Long uuid,
        int progress
) {
    public static PersonalRoadmapResponse from(Roadmap roadmap, Long uuid) {
        return new PersonalRoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                roadmap.getImageUrl(),
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                uuid,
                roadmap.getProgress()
        );
    }
}
