package odyssey.backend.presentation.directory.dto.response;

import odyssey.backend.domain.directory.Directory;
import odyssey.backend.presentation.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

public record DirectoryResponse(
        Long id,
        String name,
        Long parentId,
        List<DirectoryResponse> directories,
        List<SimpleRoadmapResponse> roadmaps
) {
    public static DirectoryResponse from(Directory directory) {
        return new DirectoryResponse(
                directory.getId(),
                directory.getName(),
                directory.getParent() != null ? directory.getParent().getId() : null,
                directory.getChildren() != null
                        ? directory.getChildren().stream()
                        .map(DirectoryResponse::from)
                        .toList()
                        : List.of(),
                directory.getRoadmaps() != null
                        ? directory.getRoadmaps().stream()
                        .map(SimpleRoadmapResponse::from)
                        .toList()
                        : List.of()
        );
    }
}
