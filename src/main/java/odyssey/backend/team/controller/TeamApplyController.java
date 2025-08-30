package odyssey.backend.team.controller;

import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.team.dto.response.ApplyResponse;
import odyssey.backend.team.service.TeamApplyService;
import odyssey.backend.user.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class TeamApplyController {

    private final TeamApplyService teamApplyService;

    @PostMapping("/{teamId}")
    public SingleCommonResponse<ApplyResponse> apply(
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(teamApplyService.apply(teamId, user));
    }

    @PutMapping("/{applyId}")
    public SingleCommonResponse<ApplyResponse> approved(
            @PathVariable Long applyId,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(teamApplyService.approve(applyId, user));
    }

    @PatchMapping("/{applyId}")
    public SingleCommonResponse<String> reject(
            @PathVariable Long applyId,
            @AuthenticationPrincipal User user
    ){
        teamApplyService.reject(applyId, user);

        return CommonResponse.ok("삭제되었습니다.");
    }
}
