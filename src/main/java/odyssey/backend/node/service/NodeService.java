package odyssey.backend.node.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.node.dto.NodeRequest;
import odyssey.backend.node.dto.NodeResponse;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final RoadmapRepository roadmapRepository;
    private final NodeRepository nodeRepository;

    public NodeResponse createNode(Long roadmapId ,NodeRequest request) {

        Roadmap roadmap = roadmapRepository.findById(roadmapId).get();

        Node parentNode = nodeRepository.findById(request.getParentId()).get();

        Node node = nodeRepository.save(
                Node.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .x(request.getX())
                        .y(request.getY())
                        .type(request.getType())
                        .height(request.getHeight())
                        .width(request.getWidth())
                        .roadmap(roadmap)
                        .parent(parentNode)
                        .build()
        );

        return new NodeResponse(node);
    }

    public List<NodeResponse> getNodesByRoadmapId(Long roadmapId) {

        List<Node> nodes = nodeRepository.findByRoadmapId(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("오류오류"));

        return nodes.stream()
                .map(NodeResponse::new)
                .collect(Collectors.toList());
    }

    public NodeResponse getNodeByIdAndRoadmapId(Long roadmapId, Long nodeId) {
        Node node = nodeRepository.findByIdAndRoadmapId(roadmapId, nodeId)
                .orElseThrow(() -> new IllegalArgumentException("에에엥"));
        return new NodeResponse(node);
    }

    @Transactional
    public NodeResponse updateNode(Long roadmapId, Long nodeId, NodeRequest request) {
        Node node = nodeRepository.findByIdAndRoadmapId(roadmapId, nodeId)
                .orElseThrow(() -> new IllegalArgumentException("비상비상"));

        node.update(request);

        return new NodeResponse(node);
    }

    @Transactional
    public void deleteNodeByIdAndRoadmapId(Long roadmapId, Long nodeId) {
        nodeRepository.deleteByRoadmapIdAndId(roadmapId, nodeId);
    }
}
