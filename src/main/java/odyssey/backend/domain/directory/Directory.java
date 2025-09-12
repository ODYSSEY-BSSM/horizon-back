package odyssey.backend.domain.directory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;

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

    @ManyToOne
    private User user;

    @Column(name = "team_id")
    private Long teamId;

    public static Directory from(DirectoryRequest request, Directory parent, User user) {
        return new Directory(request.getName(), parent, user, null);
    }

    public static Directory fromTeam(DirectoryRequest request, Directory parent, Long teamId) {
        return new Directory(request.getName(), parent, null, teamId);
    }

    Directory(String name, Directory parent, User user, Long teamId) {
        this.name = name;
        this.parent = parent;
        this.user = user;
        this.teamId = teamId;
    }

    public void update(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }
}
