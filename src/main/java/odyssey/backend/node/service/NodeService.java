package odyssey.backend.node.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.global.exception.NodeNotFoundException;
import odyssey.backend.global.exception.RoadmapNotFoundException;
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
@RequiredArgsConstructor
public class NodeService {

    private final RoadmapRepository roadmapRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public NodeResponse createNode(Long roadmapId ,NodeRequest request) {

        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);

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

        return new NodeResponse(node);
    }

    @Transactional
    public List<NodeResponse> getNodesByRoadmapId(Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);

        roadmap.updateLastAccessedAt();

        List<Node> nodes = nodeRepository.findByRoadmapId(roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        return nodes.stream()
                .map(NodeResponse::new)
                .toList();
    }

    public NodeResponse getNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);
        return new NodeResponse(node);
    }

    @Transactional
    public NodeResponse updateNode(Long nodeId, Long roadmapId, NodeRequest request) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);

        roadmap.updateLastModifiedAt();

        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        node.update(request);

        return new NodeResponse(node);
    }

    @Transactional
    public void deleteNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);

        nodeRepository.delete(node);
    }

}
