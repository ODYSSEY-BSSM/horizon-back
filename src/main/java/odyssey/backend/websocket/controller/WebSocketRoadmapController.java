package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.roadmap.RoadmapCreatedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapDeletedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdatedNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/roadmap")
public class WebSocketRoadmapController {

    private final SimpMessagingTemplate messaging;

    @MessageMapping("/created")
    public void notifyCreate(@Valid RoadmapCreatedNotification notification) {
        messaging.convertAndSend("/topic/roadmap/created", notification);
    }

    @MessageMapping("/updated")
    public void update(@Valid RoadmapUpdatedNotification notification) {
        messaging.convertAndSend("/topic/roadmap/updated", notification);
    }

    @MessageMapping("/deleted")
    public void notifyDelete(@Valid RoadmapDeletedNotification notification) {
        messaging.convertAndSend("/topic/roadmap/deleted", notification.getRoadmapId());
    }
}
