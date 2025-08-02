package odyssey.backend.roadmap.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.image.domain.Image;
import odyssey.backend.node.domain.Node;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime lastAccessedAt;

    @OneToOne(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Node> nodes;

    @ManyToOne
    @JoinColumn(name = "directory_id")
    private Directory directory;

    public static Roadmap from(RoadmapRequest request, Directory directory) {
        return new Roadmap(request.getTitle(), request.getDescription(), request.getCategories(), directory);
    }

    Roadmap(String title, String description, List<String> categories, Directory directory) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.isFavorite = false;
        this.lastAccessedAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDate.now();
        this.directory = directory;
    }

    public void update(String title, String description, List<String> categories) {
        this.title = title;
        this.description = description;
        this.categories = categories;
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    public void updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    public void updateLastModifiedAt() {
        this.lastModifiedAt = LocalDate.now();
    }

}
