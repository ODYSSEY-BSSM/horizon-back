package odyssey.backend.roadmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoadmapResponse {

    private Long id;

    private String title;

    private String description;

    private List<String> categories;

    private String thumbnailUrl;

    private LocalDate lastModifiedAt;

    private LocalDateTime lastAccessedAt;

    private Boolean isFavorite;

    private String location;

    public RoadmapResponse(Roadmap roadmap, String thumbnailUrl) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.description = roadmap.getDescription();
        this.categories = roadmap.getCategories();
        this.lastModifiedAt = roadmap.getLastModifiedAt();
        this.lastAccessedAt = roadmap.getLastAccessedAt();
        this.isFavorite = roadmap.getIsFavorite();
        this.location = "내 로드맵";
        this.thumbnailUrl = thumbnailUrl;
    }

}
