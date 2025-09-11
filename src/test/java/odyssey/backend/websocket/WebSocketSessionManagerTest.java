package odyssey.backend.websocket;

import odyssey.backend.infrastructure.websocket.WebSocketSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketSessionManagerTest {

    private WebSocketSessionManager sessionManager;
    private SimpMessagingTemplate messaging;

    @BeforeEach
    void setUp() {
        messaging = mock(SimpMessagingTemplate.class);
        sessionManager = new WebSocketSessionManager();
    }

    @Test
    void 세션_연결을_관리한다() {
        String sessionId = "session1";
        Long userId = 1L;

        sessionManager.addSession(sessionId, userId);

        assertFalse(sessionManager.getSessionsByUserId(userId).isEmpty());
    }

    @Test
    void 팀_구독을_관리한다() {
        String sessionId = "session1";
        Long userId = 1L;
        Long teamId = 100L;

        sessionManager.addSession(sessionId, userId);
        sessionManager.subscribeToTeam(userId, teamId);

        assertTrue(sessionManager.getUsersSubscribedToTeam(teamId).contains(userId));
    }

    @Test
    void 로드맵_구독을_관리한다() {
        String sessionId = "session1";
        Long userId = 1L;
        Long roadmapId = 200L;

        sessionManager.addSession(sessionId, userId);
        sessionManager.subscribeToRoadmap(userId, roadmapId);

        assertTrue(sessionManager.getUsersSubscribedToRoadmap(roadmapId).contains(userId));
    }

    @Test
    void 세션_종료시_구독이_정리된다() {
        String sessionId = "session1";
        Long userId = 1L;
        Long teamId = 100L;

        sessionManager.addSession(sessionId, userId);
        sessionManager.subscribeToTeam(userId, teamId);
        sessionManager.removeSession(sessionId);

        assertTrue(sessionManager.getSessionsByUserId(userId).isEmpty());
        assertFalse(sessionManager.getUsersSubscribedToTeam(teamId).contains(userId));
    }

    @Test
    void 여러_세션_중_하나만_종료시_구독은_유지된다() {
        String sessionId1 = "session1";
        String sessionId2 = "session2";
        Long userId = 1L;
        Long teamId = 100L;

        sessionManager.addSession(sessionId1, userId);
        sessionManager.addSession(sessionId2, userId);
        sessionManager.subscribeToTeam(userId, teamId);
        sessionManager.removeSession(sessionId1);

        assertFalse(sessionManager.getSessionsByUserId(userId).isEmpty());
        assertTrue(sessionManager.getUsersSubscribedToTeam(teamId).contains(userId));
    }

    @Test
    void 팀_구독_해제가_정상_동작한다() {
        String sessionId = "session1";
        Long userId = 1L;
        Long teamId = 100L;

        sessionManager.addSession(sessionId, userId);
        sessionManager.subscribeToTeam(userId, teamId);
        sessionManager.unsubscribeFromTeam(userId, teamId);

        assertFalse(sessionManager.getUsersSubscribedToTeam(teamId).contains(userId));
    }

    @Test
    void 로드맵_구독_해제가_정상_동작한다() {
        String sessionId = "session1";
        Long userId = 1L;
        Long roadmapId = 200L;

        sessionManager.addSession(sessionId, userId);
        sessionManager.subscribeToRoadmap(userId, roadmapId);
        sessionManager.unsubscribeFromRoadmap(userId, roadmapId);

        assertFalse(sessionManager.getUsersSubscribedToRoadmap(roadmapId).contains(userId));
    }
}