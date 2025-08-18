package odyssey.backend.directory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.user.domain.User;

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

    public static Directory from(DirectoryRequest request, Directory parent, User user) {
        return new Directory(request.getName(), parent, user);
    }

    Directory(String name, Directory parent, User user) {
        this.name = name;
        this.parent = parent;
        this.user = user;
    }

    public void update(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }
}
