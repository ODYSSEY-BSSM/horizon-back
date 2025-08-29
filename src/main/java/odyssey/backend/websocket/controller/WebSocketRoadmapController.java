package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.roadmap.RoadmapCreateNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapDeleteNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdateNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/roadmap")
public class WebSocketRoadmapController {

    private final SimpMessagingTemplate messaging;

    @MessageMapping("/created")
    public void notifyCreate(@Valid RoadmapCreateNotification notification) {
        messaging.convertAndSend("/topic/roadmap/created", notification);
    }

    @MessageMapping("/updated")
    public void update(@Valid RoadmapUpdateNotification notification) {
        messaging.convertAndSend("/topic/roadmap/updated", notification);
    }

    @MessageMapping("/deleted")
    public void notifyDelete(@Valid RoadmapDeleteNotification notification) {
        messaging.convertAndSend("/topic/roadmap/deleted", notification.getRoadmapId());
    }
}
