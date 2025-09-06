package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.cursor.CursorMessage;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@MessageMapping("/cursor")
public class WebSocketCursorController {

    private final SimpMessagingTemplate messaging;
    private final WebSocketSessionManager sessionManager;

    @MessageMapping("/cursor")
    public void handleCursor(@Valid CursorMessage cursorMessage) {
        Set<String> subscribedUsers = sessionManager.getUsersSubscribedToRoadmap(cursorMessage.getRoadmapId());
        for (String userId : subscribedUsers) {
            messaging.convertAndSendToUser(userId, "/topic/cursor/" + cursorMessage.getRoadmapId(), cursorMessage);
        }
    }
}
