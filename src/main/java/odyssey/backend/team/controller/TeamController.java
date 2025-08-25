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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
