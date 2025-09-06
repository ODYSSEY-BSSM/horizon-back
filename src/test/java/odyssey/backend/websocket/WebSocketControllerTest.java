package odyssey.backend.websocket;

import odyssey.backend.websocket.controller.WebSocketCursorController;
import odyssey.backend.websocket.controller.WebSocketSubscriptionController;
import odyssey.backend.websocket.dto.cursor.CursorMessage;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WebSocketSessionManager sessionManager;

    @InjectMocks
    private WebSocketCursorController cursorController;

    @InjectMocks
    private WebSocketSubscriptionController subscriptionController;

    @Test
    void 커서_움직임을_구독자들에게_전송한다() {
        // given
        Long roadmapId = 1L;
        CursorMessage cursorMessage = new CursorMessage(roadmapId, 400, 500);

        given(sessionManager.getUsersSubscribedToRoadmap(roadmapId)).willReturn(Set.of("user2", "user3"));

        // when
        cursorController.handleCursor(cursorMessage);

        // then
        verify(messagingTemplate, times(2)).convertAndSendToUser(
                anyString(),
                eq("/topic/cursor/" + roadmapId),
                eq(cursorMessage)
        );
    }

    @Test
    void 팀_구독을_처리한다() {
        // given
        Long teamId = 1L;
        String sessionId = "session123";
        String userId = "user1";
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        
        given(headerAccessor.getSessionId()).willReturn(sessionId);
        given(sessionManager.getUserBySession(sessionId)).willReturn(userId);

        // when
        subscriptionController.subscribeToTeam(teamId, headerAccessor);

        // then
        verify(sessionManager).subscribeToTeam(userId, teamId);
    }

    @Test
    void 로드맵_구독을_처리한다() {
        // given
        Long roadmapId = 1L;
        String sessionId = "session123";
        String userId = "user1";
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        
        given(headerAccessor.getSessionId()).willReturn(sessionId);
        given(sessionManager.getUserBySession(sessionId)).willReturn(userId);

        // when
        subscriptionController.subscribeToRoadmap(roadmapId, headerAccessor);

        // then
        verify(sessionManager).subscribeToRoadmap(userId, roadmapId);
    }

    @Test
    void 팀_구독_해제를_처리한다() {
        // given
        Long teamId = 1L;
        String sessionId = "session123";
        String userId = "user1";
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        
        given(headerAccessor.getSessionId()).willReturn(sessionId);
        given(sessionManager.getUserBySession(sessionId)).willReturn(userId);

        // when
        subscriptionController.unsubscribeFromTeam(teamId, headerAccessor);

        // then
        verify(sessionManager).unsubscribeFromTeam(userId, teamId);
    }

    @Test
    void 로드맵_구독_해제를_처리한다() {
        // given
        Long roadmapId = 1L;
        String sessionId = "session123";
        String userId = "user1";
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        
        given(headerAccessor.getSessionId()).willReturn(sessionId);
        given(sessionManager.getUserBySession(sessionId)).willReturn(userId);

        // when
        subscriptionController.unsubscribeFromRoadmap(roadmapId, headerAccessor);

        // then
        verify(sessionManager).unsubscribeFromRoadmap(userId, roadmapId);
    }
}