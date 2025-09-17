package odyssey.backend.presentation.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.websocket.WebSocketSessionManager;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketSubscriptionController {

    private final WebSocketSessionManager sessionManager;
    private final TeamService teamService;

    @SubscribeMapping("/topic/directory/team/{teamId}/created")
    public void subscribeToTeamDirectoryCreated(@DestinationVariable Long teamId,
                                                @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("디렉토리 생성 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @SubscribeMapping("/topic/directory/team/{teamId}/updated")
    public void subscribeToTeamDirectoryUpdated(@DestinationVariable Long teamId,
                                                @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("디렉토리 업데이트 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @SubscribeMapping("/topic/directory/team/{teamId}/deleted")
    public void subscribeToTeamDirectoryDeleted(@DestinationVariable Long teamId,
                                                @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("디렉토리 삭제 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @SubscribeMapping("/topic/node/team/{teamId}/created")
    public void subscribeToTeamNodeCreated(@DestinationVariable Long teamId,
                                           @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("노드 생성 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @SubscribeMapping("/topic/node/team/{teamId}/updated")
    public void subscribeToTeamNodeUpdated(@DestinationVariable Long teamId,
                                           @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("노드 업데이트 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @SubscribeMapping("/topic/node/team/{teamId}/deleted")
    public void subscribeToTeamNodeDeleted(@DestinationVariable Long teamId,
                                           @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("노드 삭제 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }
        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @MessageMapping("/subscribe/team/{teamId}")
    public void subscribeToTeam(@DestinationVariable Long teamId, 
                              @AuthenticationPrincipal User user) {
        if (!teamService.isUserMemberOfTeam(user.getUuid(), teamId)) {
            log.warn("팀 구독 권한 없음 - 사용자ID: {}, 팀ID: {}", user.getUuid(), teamId);
            throw new AccessDeniedException("해당 팀의 멤버가 아닙니다: " + teamId);
        }

        sessionManager.subscribeToTeam(user.getUuid(), teamId);
    }

    @MessageMapping("/unsubscribe/team/{teamId}")
    public void unsubscribeFromTeam(@DestinationVariable Long teamId,
                                  @AuthenticationPrincipal User user) {
        sessionManager.unsubscribeFromTeam(user.getUuid(), teamId);
    }

    @MessageMapping("/subscribe/roadmap/{roadmapId}")
    public void subscribeToRoadmap(@DestinationVariable Long roadmapId,
                                 @AuthenticationPrincipal User user) {
        sessionManager.subscribeToRoadmap(user.getUuid(), roadmapId);
    }

    @MessageMapping("/unsubscribe/roadmap/{roadmapId}")
    public void unsubscribeFromRoadmap(@DestinationVariable Long roadmapId,
                                     @AuthenticationPrincipal User user) {
        sessionManager.unsubscribeFromRoadmap(user.getUuid(), roadmapId);
    }
}
