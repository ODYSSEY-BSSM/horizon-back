package odyssey.backend.node.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.node.exception.NodeNotFoundException;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
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
                Node.from(request, roadmap, parentNode)
        );

        log.info("만들어진 노드 Id : {} ", node.getId());

        return NodeResponse.from(node);
    }

    @Transactional
    public List<NodeResponse> getNodesByRoadmapId(Long roadmapId) {
        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastAccessedAt();

        List<Node> nodes = nodeRepository.findByRoadmapId(roadmapId);

        return nodes.stream()
                .filter(node -> node.getParent() == null)
                .map(NodeResponse::from)
                .toList();
    }

    public NodeResponse getNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = findByIdAndRoadmapId(nodeId, roadmapId);

        log.info("조회된 노드 Id : {} ", node.getId());

        return NodeResponse.from(node);
    }

    @Transactional
    public NodeResponse updateNode(Long nodeId, Long roadmapId, NodeRequest request) {
        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastModifiedAt();

        Node node = findByIdAndRoadmapId(nodeId, roadmapId);

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

        return NodeResponse.from(node);
    }

    @Transactional
    public void deleteNodeByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        Node node = findByIdAndRoadmapId(nodeId, roadmapId);

        Roadmap roadmap = getRoadmapById(roadmapId);

        roadmap.updateLastModifiedAt();

        log.info("삭제된 노드 Id : {} ", node.getId());

        nodeRepository.delete(node);
    }

    private Roadmap getRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                        .orElseThrow(RoadmapNotFoundException::new);
    }

    private Node findByIdAndRoadmapId(Long nodeId, Long roadmapId) {
        return nodeRepository.findByIdAndRoadmapId(nodeId, roadmapId)
                .orElseThrow(NodeNotFoundException::new);
    }

}
