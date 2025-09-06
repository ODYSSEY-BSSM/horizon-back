package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.node.*;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@MessageMapping("/node")
public class WebSocketNodeController {

    private final SimpMessagingTemplate messaging;
    private final WebSocketSessionManager sessionManager;

    @MessageMapping("/created")
    public void notifyNodeCreated(@Valid NodeCreatedNotification notification) {
        sendNodeNotification("created", notification.getRoadmapId(), notification);
    }

    @MessageMapping("/updated")
    public void notifyNodeUpdated(@Valid NodeUpdatedNotification notification) {
        sendNodeNotification("updated", notification.getRoadmapId(), notification);
    }

    @MessageMapping("/deleted")
    public void notifyNodeDeleted(@Valid NodeDeletedNotification notification) {
        sendNodeNotification("deleted", notification.getRoadmapId(), notification.getNodeId());
    }

    @MessageMapping("/selection")
    public void handleNodeSelection(@Valid NodeSelectionMessage selectionMessage) {
        sendNodeNotification("selection", selectionMessage.getRoadmapId(), selectionMessage);
    }

    @MessageMapping("/move")
    public void handleNodeMove(@Valid NodeMoveMessage moveMessage) {
        sendNodeNotification("move", moveMessage.getRoadmapId(), moveMessage);
    }

    @MessageMapping("/size")
    public void handleNodeSize(@Valid NodeSizeMessage sizeMessage) {
        sendNodeNotification("size", sizeMessage.getRoadmapId(), sizeMessage);
    }

    private void sendNodeNotification(String event, Long roadmapId, Object payload) {
        Set<String> subscribedUsers = sessionManager.getUsersSubscribedToRoadmap(roadmapId);
        for (String userId : subscribedUsers) {
            messaging.convertAndSendToUser(userId, "/topic/node/" + roadmapId + "/" + event, payload);
        }
    }

}
