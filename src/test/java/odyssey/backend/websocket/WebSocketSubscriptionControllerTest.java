package odyssey.backend.websocket;

import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.Role;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.websocket.WebSocketSessionManager;
import odyssey.backend.presentation.websocket.WebSocketSubscriptionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketSubscriptionControllerTest {

    @Mock
    private WebSocketSessionManager sessionManager;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private WebSocketSubscriptionController controller;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = createTestUser(1L, "testUser");
    }

    private User createTestUser(Long uuid, String username) {
        User user = new User("test@example.com", username, "password", Role.USER);
        try {
            var uuidField = user.getClass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(user, uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Test
    void 팀_멤버는_팀_구독이_가능하다() {
        Long teamId = 1L;
        given(teamService.isUserMemberOfTeam(testUser.getUuid(), teamId)).willReturn(true);

        controller.subscribeToTeam(teamId, testUser);

        verify(sessionManager).subscribeToTeam(testUser.getUuid(), teamId);
    }

    @Test
    void 팀_멤버가_아니면_팀_구독이_불가능하다() {
        Long teamId = 1L;
        given(teamService.isUserMemberOfTeam(testUser.getUuid(), teamId)).willReturn(false);

        assertThrows(AccessDeniedException.class, () -> {
            controller.subscribeToTeam(teamId, testUser);
        });

        verify(sessionManager, never()).subscribeToTeam(any(), any());
    }

    @Test
    void 팀_구독_해제는_항상_가능하다() {
        Long teamId = 1L;

        controller.unsubscribeFromTeam(teamId, testUser);

        verify(sessionManager).unsubscribeFromTeam(testUser.getUuid(), teamId);
    }

    @Test
    void 로드맵_구독은_권한_검증_없이_가능하다() {
        Long roadmapId = 1L;

        controller.subscribeToRoadmap(roadmapId, testUser);

        verify(sessionManager).subscribeToRoadmap(testUser.getUuid(), roadmapId);
    }

    @Test
    void 로드맵_구독_해제는_권한_검증_없이_가능하다() {
        Long roadmapId = 1L;

        controller.unsubscribeFromRoadmap(roadmapId, testUser);

        verify(sessionManager).unsubscribeFromRoadmap(testUser.getUuid(), roadmapId);
    }
}