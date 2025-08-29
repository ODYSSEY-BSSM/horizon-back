package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.cursor.CursorMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/cursor")
public class WebSocketCursorController {

    private final SimpMessagingTemplate messaging;

    @MessageMapping("/cursor")
    public void handleCursor(@Valid CursorMessage cursorMessage) {
        messaging.convertAndSend("/topic/cursor/" + cursorMessage.getRoadmapId(), cursorMessage);
    }
}
