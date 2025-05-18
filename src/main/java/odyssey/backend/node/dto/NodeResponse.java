package odyssey.backend.node.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.node.domain.Node;
import odyssey.backend.node.domain.NodeType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class NodeResponse {

    private Long id;

    private String title;

    private String description;

    private int height;

    private int width;

    private NodeType type;

    private int x;

    private int y;

    private Long roadmapId;

    private Long parentNodeId;

    private List<NodeResponse> childNode;

    public NodeResponse(Node node){
        this.id = node.getId();
        this.title = node.getTitle();
        this.description = node.getDescription();
        this.height = node.getHeight();
        this.width = node.getWidth();
        this.type = node.getType();
        this.x = node.getX();
        this.y = node.getY();
        this.roadmapId = node.getRoadmap().getId();
        this.parentNodeId = node.getParent() != null ? node.getParent().getId() : null;
        this.childNode = node.getChildren() != null
                ? node.getChildren().stream()
                .map(NodeResponse::new)
                .collect(Collectors.toList())
                : List.of();
    }
}
