package odyssey.backend.domain.problem;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.domain.node.Node;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tbl_problem")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    Problem(String title, String answer, Node node){
        this.title = title;
        this.answer = answer;
        this.node = node;
        status = Status.UNRESOLVED;
    }

    public static Problem from(ProblemRequest request, Node node){
        return new Problem(
                request.getTitle(),
                request.getAnswer(),
                node
        );
    }

    public boolean isCorrect(String answer){
        if(answer.equals(this.answer)){
            status = Status.RESOLVED;
            return true;
        }
        return false;
    }

}
