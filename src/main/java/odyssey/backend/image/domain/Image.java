package odyssey.backend.image.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import odyssey.backend.roadmap.domain.Roadmap;

@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "roadmap_id", unique = true)
    private Roadmap roadmap;

    public String getUrl(){
        return this.url;
    }

    Image(String url, Roadmap roadmap) {
        this.url = url;
        this.roadmap = roadmap;
    }

    public static Image ok(String url, Roadmap roadmap) {
        return new Image(url, roadmap);
    }

}
