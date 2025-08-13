package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.user.Repository.UserRepository;
import odyssey.backend.websocket.dto.RoadmapCrudMessage;
import odyssey.backend.websocket.service.WebSocketRoadmapService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketRoadmapController {

    private final WebSocketRoadmapService webSocketRoadmapService;
    private final SimpMessagingTemplate messaging;

    @MessageMapping("/roadmap/{roadmapId}/crud")
    public void handleRoadmapCrud(@DestinationVariable Long roadmapId, @Valid RoadmapCrudMessage message) {
        RoadmapCrudMessage roadmapMessage = webSocketRoadmapService.handleCrud(roadmapId, message);

        messaging.convertAndSend("/topic/roadmap/" + roadmapId + "/crud", roadmapMessage);
    }

}
