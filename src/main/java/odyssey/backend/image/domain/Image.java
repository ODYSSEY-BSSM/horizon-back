package odyssey.backend.image.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odyssey.backend.roadmap.domain.Roadmap;

@Entity
@Setter
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

}
