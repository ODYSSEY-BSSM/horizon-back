package odyssey.backend.directory.dto.response;

import lombok.Getter;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

@Getter
public class DirectoryResponse {

    private final Long id;

    private final String name;

    private final Long parentId;

    private final List<DirectoryResponse> directories;

    private final List<SimpleRoadmapResponse> roadmaps;

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
                        .map(SimpleRoadmapResponse::new)
                        .toList()
                        : List.of()
        );
    }

    DirectoryResponse(Long id,
                      String name,
                      Long parentId,
                      List<DirectoryResponse> directories,
                      List<SimpleRoadmapResponse> roadmaps) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.directories = directories;
        this.roadmaps = roadmaps;
    }

}
