package odyssey.backend.domain.roadmap;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.node.Node;
import odyssey.backend.domain.team.Team;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;

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

    private String imageUrl;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Node> nodes;

    @ManyToOne
    @JoinColumn(name = "directory_id", nullable = false)
    private Directory directory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public static Roadmap from(RoadmapRequest request, String url, Directory directory, User user, Team team) {
        return new Roadmap(request.getTitle(), request.getDescription(), request.getCategories(), url, directory, user, team);
    }

    Roadmap(String title, String description, List<String> categories, String url, Directory directory, User user, Team team) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.imageUrl = url;
        this.isFavorite = false;
        this.lastAccessedAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDate.now();
        this.directory = directory;
        this.user = user;
        this.team = team;
    }

    public void update(String title, String description, List<String> categories) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        updateLastModifiedAt();
    }

    public void changeDirectory(Directory directory) {
        this.directory = directory;
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
