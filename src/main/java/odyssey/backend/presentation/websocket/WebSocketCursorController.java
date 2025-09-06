package odyssey.backend.presentation.websocket;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.websocket.dto.CursorMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/cursor")
public class WebSocketCursorController {

    private final SimpMessagingTemplate messaging;

    @MessageMapping
    public void handleCursor(@Valid CursorMessage cursorMessage, 
                           @AuthenticationPrincipal User user) {
        CursorWithUserMessage message = new CursorWithUserMessage(
            cursorMessage.getRoadmapId(),
            cursorMessage.getX(),
            cursorMessage.getY(),
            user.getUuid(),
            user.getUsername()
        );
        
        messaging.convertAndSend("/topic/cursor/" + cursorMessage.getRoadmapId(), message);
    }
    
    public record CursorWithUserMessage(
        Long roadmapId,
        Integer x,
        Integer y,
        Long userId,
        String username
    ) {}
}
