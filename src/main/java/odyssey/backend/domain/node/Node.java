package odyssey.backend.domain.node;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.domain.problem.Problem;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.presentation.node.dto.request.NodeRequest;

import java.util.ArrayList;
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
    private List<Node> children = new ArrayList<>();

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Problem> problems = new ArrayList<>();

    @Column(nullable = false)
    private int progress = 0;

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

    public void addProblem(Problem problem){
        if(problems.size() == 3){
            throw new IllegalStateException("문제는 3개가 최대입니다.");
        }
        problems.add(problem);
    }

    private void updateProgress(){
        int totalProblems = problems.size();
        if(totalProblems == 0) {
            this.progress = 0;
            return;
        }
        long progressCount = problems.stream()
                .filter(Problem::isResolved)
                .count();
        this.progress = (int)Math.round((progressCount / (double) totalProblems) * 100);
    }

    public void solveProblem(Problem problem, String answer){
        if(problem.isCorrect(answer)){
            updateProgress();
            roadmap.updateProgress();
        }
    }

    public void validateNodeLocation(){
        if(this.type != NodeType.Bottom){
            throw new IllegalArgumentException("해당 노드에 문제를 만들 수 없습니다");
        }
    }

}
