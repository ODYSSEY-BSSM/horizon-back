package odyssey.backend.roadmap.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.roadmap.dto.RoadmapRequest;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "roadmap_tbl")
@NoArgsConstructor
@Getter
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false, length = 150)
    private String description;

    @ElementCollection
    @CollectionTable(name = "roadmap_category", joinColumns = @JoinColumn(name = "roadmap_id"))
    @Column(name = "category")
    private List<String> categories;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    @Column(name = "last_modified_at")
    private LocalDate lastModifiedAt;

    @Column(name = "last_accessed_at")
    private LocalDate lastAccessedAt;

    @Builder
    public Roadmap(String title, String description, List<String> categories) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.isFavorite = false;
        this.lastModifiedAt = LocalDate.now();
    }

    public void update(RoadmapRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.categories = request.getCategories();
        this.lastModifiedAt = LocalDate.now();
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    public void setLastAccessedAt() {
        this.lastAccessedAt = LocalDate.now();
    }

    public void setLastModifiedAt() {
        this.lastModifiedAt = LocalDate.now();
    }

}
