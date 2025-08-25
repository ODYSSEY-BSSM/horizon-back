package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.roadmap.RoadmapCreateNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapDeleteNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdateMessage;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdateResponse;
import odyssey.backend.websocket.service.WebSocketRoadmapService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/roadmap")
public class WebSocketRoadmapController {

    private final WebSocketRoadmapService webSocketRoadmapService;
    private final SimpMessagingTemplate messaging;

    @MessageMapping("/created")
    public void notifyCreate(RoadmapCreateNotification notification) {
        messaging.convertAndSend("/topic/roadmap/created", notification);
    }

    @MessageMapping("/update")
    public void update(@Valid RoadmapUpdateMessage message) {
        RoadmapUpdateResponse response = webSocketRoadmapService.update(message);
        messaging.convertAndSend("/topic/roadmap/update", response);
    }

    @MessageMapping("/deleted")
    public void notifyDelete(RoadmapDeleteNotification notification) {
        messaging.convertAndSend("/topic/roadmap/deleted", notification.getRoadmapId());
    }
}
