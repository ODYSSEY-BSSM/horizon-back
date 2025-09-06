package odyssey.backend.websocket.controller;

import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketSubscriptionController {

    private final WebSocketSessionManager sessionManager;

    @MessageMapping("/subscribe/team/{teamId}")
    public void subscribeToTeam(@DestinationVariable Long teamId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = sessionManager.getUserBySession(sessionId);
        sessionManager.subscribeToTeam(userId, teamId);
    }

    @MessageMapping("/unsubscribe/team/{teamId}")
    public void unsubscribeFromTeam(@DestinationVariable Long teamId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = sessionManager.getUserBySession(sessionId);
        sessionManager.unsubscribeFromTeam(userId, teamId);
    }

    @MessageMapping("/subscribe/roadmap/{roadmapId}")
    public void subscribeToRoadmap(@DestinationVariable Long roadmapId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = sessionManager.getUserBySession(sessionId);
        sessionManager.subscribeToRoadmap(userId, roadmapId);
    }

    @MessageMapping("/unsubscribe/roadmap/{roadmapId}")
    public void unsubscribeFromRoadmap(@DestinationVariable Long roadmapId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = sessionManager.getUserBySession(sessionId);
        sessionManager.unsubscribeFromRoadmap(userId, roadmapId);
    }
}