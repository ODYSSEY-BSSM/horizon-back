package odyssey.backend.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.team.dto.request.TeamRequest;
import odyssey.backend.team.dto.response.TeamResponse;
import odyssey.backend.team.service.TeamService;
import odyssey.backend.user.domain.User;
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

    @GetMapping("/{teamId}")
    public SingleCommonResponse<TeamResponse> getTeam(
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(teamService.findById(teamId));
    }

}
