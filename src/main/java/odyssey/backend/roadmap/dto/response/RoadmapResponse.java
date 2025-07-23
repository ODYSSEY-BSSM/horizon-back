package odyssey.backend.roadmap.dto.response;

import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoadmapResponse {

    private final Long id;

    private final String title;

    private final String description;

    private final List<String> categories;

    private final String thumbnailUrl;

    private final LocalDate lastModifiedAt;

    private final LocalDateTime lastAccessedAt;

    private final boolean isFavorite;

    private final String location;

    public static RoadmapResponse from(Roadmap roadmap, String thumbnailUrl) {
        return new RoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getDescription(),
                roadmap.getCategories(),
                roadmap.getLastModifiedAt(),
                roadmap.getLastAccessedAt(),
                roadmap.getIsFavorite(),
                thumbnailUrl);
    }
    RoadmapResponse(Long id, String title, String description, List<String> categories, LocalDate lastModifiedAt,
                    LocalDateTime lastAccessedAt, Boolean isFavorite, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.lastModifiedAt = lastModifiedAt;
        this.lastAccessedAt = lastAccessedAt;
        this.isFavorite = isFavorite;
        this.location = "내 로드맵";
        this.thumbnailUrl = thumbnailUrl;
    }

}
