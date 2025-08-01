package odyssey.backend.node.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.roadmap.domain.Roadmap;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "node_tbl")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false, length = 1500)
    private String description;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer width;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeType type;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_node_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> children;

    public static Node from(NodeRequest request, Roadmap roadmap, Node parent) {
        return new Node(
                request.getTitle(),
                request.getDescription(),
                request.getHeight(),
                request.getWidth(),
                request.getType(),
                request.getX(),
                request.getY(),
                request.getCategory(),
                roadmap,
                parent
        );
    }

    Node(
            String title, String description, int height, int width, NodeType type, int x, int y, String category, Roadmap roadmap, Node parent) {
        this.title = title;
        this.description = description;
        this.height = height;
        this.width = width;
        this.type = type;
        this.x = x;
        this.y = y;
        this.category = category;
        this.roadmap = roadmap;
        this.parent = parent;
    }

    public void update(
            String title, String description, Integer height, Integer width, NodeType type, Integer x, Integer y, String category
    ){
        this.title = title;
        this.description = description;
        this.height = height;
        this.width = width;
        this.type = type;
        this.x = x;
        this.y = y;
        this.category = category;
    }
}
