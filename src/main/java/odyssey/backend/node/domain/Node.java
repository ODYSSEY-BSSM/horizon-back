package odyssey.backend.node.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.node.dto.NodeRequest;
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
    private int height;

    @Column(nullable = false)
    private int width;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeType type;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

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

    @Builder
    public Node(
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

    public void update(NodeRequest request){
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.height = request.getHeight();
        this.width = request.getWidth();
        this.type = request.getType();
        this.x = request.getX();
        this.y = request.getY();
        this.category = request.getCategory();
    }
}
