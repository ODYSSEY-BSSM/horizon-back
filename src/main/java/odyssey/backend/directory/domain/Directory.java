package odyssey.backend.directory.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.roadmap.domain.Roadmap;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "directory_tbl")
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dir_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Directory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Directory> children;

    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Roadmap> roadmaps;

    @Builder
    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public void update(String name) {
        this.name = name;
    }
}
