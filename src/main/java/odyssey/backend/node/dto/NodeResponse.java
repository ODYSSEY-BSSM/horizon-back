package odyssey.backend.node.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeType;

import java.util.List;

@Getter
@AllArgsConstructor
public class NodeResponse {

    private final Long id;

    private final String title;

    private final String description;

    private final int height;

    private final int width;

    private final NodeType type;

    private final int x;

    private final int y;

    private final String category;

    private final Long roadmapId;

    private final Long parentNodeId;

    private final List<NodeResponse> childNode;

    public NodeResponse(Node node){
        this.id = node.getId();
        this.title = node.getTitle();
        this.description = node.getDescription();
        this.height = node.getHeight();
        this.width = node.getWidth();
        this.type = node.getType();
        this.x = node.getX();
        this.y = node.getY();
        this.category = node.getCategory();
        this.roadmapId = node.getRoadmap().getId();
        this.parentNodeId = node.getParent() != null ? node.getParent().getId() : null;
        this.childNode = node.getChildren() != null
                ? node.getChildren().stream()
                .map(NodeResponse::new)
                .toList()
                : List.of();
    }
}
