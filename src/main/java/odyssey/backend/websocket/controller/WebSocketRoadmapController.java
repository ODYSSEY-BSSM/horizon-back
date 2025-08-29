package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.roadmap.RoadmapCreatedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapDeletedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdatedNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/roadmap")
public class WebSocketRoadmapController {

    @MessageMapping("/created")
    @SendTo("/topic/roadmap/created")
    public RoadmapCreatedNotification notifyCreate(@Valid RoadmapCreatedNotification notification) {
        return notification;
    }

    @MessageMapping("/updated")
    @SendTo("/topic/roadmap/updated")
    public RoadmapUpdatedNotification update(@Valid RoadmapUpdatedNotification notification) {
        return notification;
    }

    @MessageMapping("/deleted")
    @SendTo("/topic/roadmap/deleted")
    public RoadmapDeletedNotification notifyDelete(@Valid RoadmapDeletedNotification notification) {
        return notification;
    }
}
