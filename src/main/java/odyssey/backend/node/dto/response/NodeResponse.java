package odyssey.backend.node.dto.response;

import lombok.Getter;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeType;

import java.util.List;

@Getter
public class NodeResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final Integer height;
    private final Integer width;
    private final NodeType type;
    private final Integer x;
    private final Integer y;
    private final String category;
    private final Long roadmapId;
    private final Long parentNodeId;
    private final List<NodeResponse> childNode;

    private NodeResponse(Long id, String title, String description, Integer height, Integer width,
                         NodeType type, Integer x, Integer y, String category,
                         Long roadmapId, Long parentNodeId, List<NodeResponse> childNode) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.height = height;
        this.width = width;
        this.type = type;
        this.x = x;
        this.y = y;
        this.category = category;
        this.roadmapId = roadmapId;
        this.parentNodeId = parentNodeId;
        this.childNode = childNode;
    }

    public static NodeResponse from(Node node) {
        List<NodeResponse> children = node.getChildren() != null
                ? node.getChildren().stream()
                .map(NodeResponse::from)
                .toList()
                : List.of();

        return new NodeResponse(
                node.getId(),
                node.getTitle(),
                node.getDescription(),
                node.getHeight(),
                node.getWidth(),
                node.getType(),
                node.getX(),
                node.getY(),
                node.getCategory(),
                node.getRoadmap().getId(),
                node.getParent() != null ? node.getParent().getId() : null,
                children
        );
    }
}
