package odyssey.backend.roadmap.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Roadmap_tbl")
@NoArgsConstructor
@Getter
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Roadmap_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false, length = 150)
    private String description;

    @Builder
    public Roadmap(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
