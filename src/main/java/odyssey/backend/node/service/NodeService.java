package odyssey.backend.node.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.node.exception.NodeNotFoundException;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.node.dto.NodeRequest;
import odyssey.backend.node.dto.NodeResponse;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NodeService {

    private final RoadmapRepository roadmapRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public NodeResponse createNode(Long roadmapId ,NodeRequest request) {

        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastModifiedAt();

        Node parentNode = null;

        if (request.getParentNodeId() != null) {
            parentNode = nodeRepository.findById(request.getParentNodeId())
                    .orElseThrow(NodeNotFoundException::new);
        }

        Node node = nodeRepository.save(
                Node.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .x(request.getX())
                        .y(request.getY())
                        .type(request.getType())
                        .height(request.getHeight())
                        .width(request.getWidth())
                        .category(request.getCategory())
                        .roadmap(roadmap)
                        .parent(parentNode)
                        .build()
        );

        log.info("만들어진 노드 Id : {} ", node.getId());

        return new NodeResponse(node);
    }

    @Transactional
    public List<NodeResponse> getNodesByRoadmapId(Long roadmapId) {
        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastAccessedAt();

        List<Node> nodes = nodeRepository.findByRoadmapId(roadmapId);

        return nodes.stream()
                .filter(node -> node.getParent() == null)
                .map(NodeResponse::new)
                .toList();
    }

    public NodeResponse getNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        log.info("조회된 노드 Id : {} ", node.getId());

        return new NodeResponse(node);
    }

    @Transactional
    public NodeResponse updateNode(Long nodeId, Long roadmapId, NodeRequest request) {
        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastModifiedAt();

        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        node.update(
                request.getTitle(),
                request.getDescription(),
                request.getHeight(),
                request.getWidth(),
                request.getType(),
                request.getX(),
                request.getY(),
                request.getCategory());

        log.info("업데이트 노드 Id : {} ", node.getId());

        log.info("업데이트 노드 Id : {} ", node.getId());

        return new NodeResponse(node);
    }

    @Transactional
    public void deleteNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastModifiedAt();

        log.info("삭제된 노드 Id : {} ", node.getId());

        nodeRepository.delete(node);
    }

    private Roadmap getRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                        .orElseThrow(RoadmapNotFoundException::new);
    }

}
