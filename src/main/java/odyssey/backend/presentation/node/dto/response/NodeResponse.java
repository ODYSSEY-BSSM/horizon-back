package odyssey.backend.presentation.node.dto.response;

import odyssey.backend.domain.node.Node;
import odyssey.backend.domain.node.NodeType;

import java.util.List;

public record NodeResponse(
        Long id,
        String title,
        String description,
        Integer height,
        Integer width,
        NodeType type,
        Integer x,
        Integer y,
        String category,
        Long roadmapId,
        Long parentNodeId,
        List<NodeResponse> childNode,
        int progress
) {
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
                children,
                node.getProgress()
        );
    }
}
