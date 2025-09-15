package odyssey.backend.presentation.problem;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.application.problem.ProblemService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.problem.dto.request.ProblemRequest;
import odyssey.backend.presentation.problem.dto.request.SolveProblemRequest;
import odyssey.backend.presentation.problem.dto.response.ProblemResponse;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{nodeId}/problems")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping
    public SingleCommonResponse<ProblemResponse> createProblem(
            @Valid @RequestBody ProblemRequest request,
            @PathVariable Long nodeId,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(problemService.createProblem(request, nodeId));
    }

    @PatchMapping
    public SingleCommonResponse<ProblemResponse> solveProblem(
            @RequestParam Long problemId,
            @Valid @RequestBody SolveProblemRequest request,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(problemService.solveProblem(problemId, request));
    }

}
