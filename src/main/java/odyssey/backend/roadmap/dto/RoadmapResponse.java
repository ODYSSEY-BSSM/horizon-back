package odyssey.backend.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.roadmap.domain.Roadmap;

import java.time.LocalDate;
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

    private Boolean isFavorite;

    public RoadmapResponse(Roadmap roadmap, String thumbnailUrl) {
        this.id = roadmap.getId();
        this.title = roadmap.getTitle();
        this.description = roadmap.getDescription();
        this.categories = roadmap.getCategories();
        this.lastModifiedAt = roadmap.getLastModifiedAt();
        this.isFavorite = roadmap.getIsFavorite();
        this.thumbnailUrl = thumbnailUrl;
    }

}
