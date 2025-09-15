package odyssey.backend.application.problem;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.node.Node;
import odyssey.backend.domain.node.exception.NodeNotFoundException;
import odyssey.backend.domain.problem.Problem;
import odyssey.backend.infrastructure.persistence.node.NodeRepository;
import odyssey.backend.infrastructure.persistence.problem.ProblemRepository;
import odyssey.backend.presentation.problem.dto.request.ProblemRequest;
import odyssey.backend.presentation.problem.dto.request.SolveProblemRequest;
import odyssey.backend.presentation.problem.dto.response.ProblemResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public ProblemResponse solveProblem(Long id, SolveProblemRequest request) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        Node node = problem.getNode();

        String answer = request.getAnswer();

        if(problem.isCorrect(answer)){
            node.solveProblem(problem, answer);
        }

        return ProblemResponse.from(problem);
    }

    @Transactional
    public ProblemResponse createProblem(ProblemRequest request, Long nodeId){
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(NodeNotFoundException::new);

        node.validate();

        Problem problem = problemRepository.save(Problem.from(request, node));

        return ProblemResponse.from(problem);
    }

}
