package odyssey.backend.presentation.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.websocket.WebSocketSessionManager;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketSubscriptionController {

    private final WebSocketSessionManager sessionManager;
    private final TeamService teamService;

    @MessageMapping("/subscribe/team/{teamId}")
    public void subscribeToTeam(@DestinationVariable Long teamId, 
                              @AuthenticationPrincipal User user) {
        // 팀 멤버십 권한 검증
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("팀 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }

        sessionManager.subscribeToTeam(user.getUuid(), teamId);
        log.info("팀 구독 성공 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
    }

    @MessageMapping("/unsubscribe/team/{teamId}")
    public void unsubscribeFromTeam(@DestinationVariable Long teamId,
                                  @AuthenticationPrincipal User user) {
        // 구독 해제는 멤버십 검증 없이 허용 (이미 구독된 상태라면)
        sessionManager.unsubscribeFromTeam(user.getUuid(), teamId);
        log.info("팀 구독 해제 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
    }

    @MessageMapping("/subscribe/roadmap/{roadmapId}")
    public void subscribeToRoadmap(@DestinationVariable Long roadmapId,
                                 @AuthenticationPrincipal User user) {
        // TODO: 로드맵 접근 권한 검증
        sessionManager.subscribeToRoadmap(user.getUuid(), roadmapId);
        log.info("로드맵 구독 성공 - 사용자ID: {}, 로드맵ID: {}", user.getUuid(), roadmapId);
    }

    @MessageMapping("/unsubscribe/roadmap/{roadmapId}")
    public void unsubscribeFromRoadmap(@DestinationVariable Long roadmapId,
                                     @AuthenticationPrincipal User user) {
        sessionManager.unsubscribeFromRoadmap(user.getUuid(), roadmapId);
        log.info("로드맵 구독 해제 - 사용자ID: {}, 로드맵ID: {}", user.getUuid(), roadmapId);
    }
}