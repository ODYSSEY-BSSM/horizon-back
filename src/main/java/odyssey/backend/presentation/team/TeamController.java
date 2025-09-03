package odyssey.backend.presentation.team;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import odyssey.backend.presentation.team.dto.request.TeamRequest;
import odyssey.backend.presentation.team.dto.response.TeamResponse;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public SingleCommonResponse<TeamResponse> create(
            @RequestBody @Valid TeamRequest request,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(teamService.create(request, user));
    }

    @DeleteMapping("/{id}")
    public SingleCommonResponse<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user){
        teamService.delete(id, user);
        return CommonResponse.ok("삭제되었습니다");
    }

    @GetMapping("/{id}")
    public SingleCommonResponse<TeamResponse> getTeam(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(teamService.findById(id));
    }

}
