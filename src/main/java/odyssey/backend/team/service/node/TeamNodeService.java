package odyssey.backend.team.service.node;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.node.dto.request.NodeMoveRequest;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.request.NodeResizeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
import odyssey.backend.node.exception.NodeNotFoundException;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.websocket.dto.node.*;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamNodeService {

    private final NodeRepository nodeRepository;
    private final RoadmapRepository roadmapRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @Transactional
    public NodeResponse createNode(Long teamId, Long roadmapId, NodeRequest request) {
        Roadmap roadmap = findRoadmapById(roadmapId);
        roadmap.updateLastModifiedAt();

        Node parentNode = null;
        if (request.getParentNodeId() != null) {
            parentNode = nodeRepository.findById(request.getParentNodeId())
                    .orElseThrow(NodeNotFoundException::new);
        }

        Node node = nodeRepository.save(Node.from(request, roadmap, parentNode));

        NodeCreatedNotification notification = NodeCreatedNotification.builder()
                .id(node.getId())
                .roadmapId(roadmapId)
                .title(node.getTitle())
                .description(node.getDescription())
                .x(node.getX())
                .y(node.getY())
                .width(node.getWidth())
                .height(node.getHeight())
                .type(node.getType())
                .category(node.getCategory())
                .build();

        sendNodeNotificationToTeam(roadmapId, "created", notification);

        log.info("팀 노드 생성 완료 - 팀ID: {}, 로드맵ID: {}, 노드ID: {}, 제목: {}", teamId, roadmapId, node.getId(), node.getTitle());
        return NodeResponse.from(node);
    }

    @Transactional(readOnly = true)
    public List<NodeResponse> getNodes(Long roadmapId) {
        Roadmap roadmap = findRoadmapById(roadmapId);
        roadmap.updateLastAccessedAt();

        List<Node> nodes = nodeRepository.findByRoadmapId(roadmapId);
        
        return nodes.stream()
                .filter(node -> node.getParent() == null)
                .map(NodeResponse::from)
                .toList();
    }

    @Transactional
    public NodeResponse updateNode(Long teamId, Long roadmapId, Long nodeId, NodeRequest request) {
        Roadmap roadmap = findRoadmapById(roadmapId);
        roadmap.updateLastModifiedAt();
        
        Node node = findNodeByIdAndRoadmapId(nodeId, roadmapId);
        
        node.update(
            request.getTitle(),
            request.getDescription(),
            request.getHeight(),
            request.getWidth(),
            request.getType(),
            request.getX(),
            request.getY(),
            request.getCategory()
        );

        NodeUpdatedNotification notification = NodeUpdatedNotification.builder()
                .id(node.getId())
                .roadmapId(roadmapId)
                .title(node.getTitle())
                .description(node.getDescription())
                .x(node.getX())
                .y(node.getY())
                .width(node.getWidth())
                .height(node.getHeight())
                .type(node.getType())
                .category(node.getCategory())
                .build();

        sendNodeNotificationToTeam(roadmapId, "updated", notification);

        log.info("팀 노드 수정 완료 - 팀ID: {}, 로드맵ID: {}, 노드ID: {}, 제목: {}", teamId, roadmapId, nodeId, node.getTitle());
        return NodeResponse.from(node);
    }

    @Transactional
    public void deleteNode(Long teamId, Long roadmapId, Long nodeId) {
        Roadmap roadmap = findRoadmapById(roadmapId);
        roadmap.updateLastModifiedAt();
        
        Node node = findNodeByIdAndRoadmapId(nodeId, roadmapId);
        String nodeTitle = node.getTitle();
        nodeRepository.delete(node);

        NodeDeletedNotification notification = new NodeDeletedNotification(nodeId, roadmapId);

        sendNodeNotificationToTeam(roadmapId, "deleted", notification);

        log.info("팀 노드 삭제 완료 - 팀ID: {}, 로드맵ID: {}, 노드ID: {}, 제목: {}", teamId, roadmapId, nodeId, nodeTitle);
    }

    @Transactional
    public NodeResponse moveNode(Long teamId, Long roadmapId, Long nodeId, NodeMoveRequest request) {
        Node node = findNodeByIdAndRoadmapId(nodeId, roadmapId);
        node.updatePosition(request.getX(), request.getY());

        NodeMoveMessage moveMessage = NodeMoveMessage.builder()
                .id(nodeId)
                .roadmapId(roadmapId)
                .x(request.getX())
                .y(request.getY())
                .parentNodeId(request.getParentNodeId())
                .build();

        sendNodeNotificationToTeam(roadmapId, "move", moveMessage);

        log.info("팀 노드 이동 완료 - 팀ID: {}, 로드맵ID: {}, 노드ID: {}, 위치: ({}, {})", teamId, roadmapId, nodeId, request.getX(), request.getY());
        return NodeResponse.from(node);
    }


    @Transactional
    public NodeResponse resizeNode(Long teamId, Long roadmapId, Long nodeId, NodeResizeRequest request) {
        Node node = findNodeByIdAndRoadmapId(nodeId, roadmapId);
        node.updateSize(request.getWidth(), request.getHeight());

        NodeSizeMessage sizeMessage = NodeSizeMessage.builder()
                .id(nodeId)
                .roadmapId(roadmapId)
                .width(request.getWidth())
                .height(request.getHeight())
                .build();
        sendNodeNotificationToTeam(roadmapId, "size", sizeMessage);

        log.info("팀 노드 크기 변경 완료 - 팀ID: {}, 로드맵ID: {}, 노드ID: {}, 크기: {}x{}", teamId, roadmapId, nodeId, request.getWidth(), request.getHeight());
        return NodeResponse.from(node);
    }

    private Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);
    }

    private Node findNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        return nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);
    }

    private void sendNodeNotificationToTeam(Long roadmapId, String event, Object payload) {
        Set<String> subscribedUsers = sessionManager.getUsersSubscribedToRoadmap(roadmapId);
        for (String userId : subscribedUsers) {
            messagingTemplate.convertAndSendToUser(userId, "/topic/node/" + roadmapId + "/" + event, payload);
        }
    }
}