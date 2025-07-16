package odyssey.backend.directory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class DirectoryResponse {

    private final Long id;

    private final String name;

    private final Long parentId;

    private final List<DirectoryResponse> directories;

    private final List<SimpleRoadmapResponse> roadmaps;

    public DirectoryResponse(Directory directory) {
        this.id = directory.getId();
        this.name = directory.getName();
        this.parentId = directory.getParent() != null ? directory.getParent().getId() : null;
        this.directories = directory.getChildren() != null
                ? directory.getChildren().stream()
                .map(DirectoryResponse::new)
                .toList()
                : List.of();

        this.roadmaps = directory.getRoadmaps() != null
                ? directory.getRoadmaps().stream()
                .map(SimpleRoadmapResponse::new)
                .toList()
                : List.of();
    }

}
