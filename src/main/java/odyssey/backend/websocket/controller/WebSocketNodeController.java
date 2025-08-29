package odyssey.backend.websocket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.websocket.dto.node.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/node")
public class WebSocketNodeController {

    private final SimpMessagingTemplate messaging;

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
        messaging.convertAndSend("/topic/node/" + roadmapId + "/" + event, payload);
    }

}
