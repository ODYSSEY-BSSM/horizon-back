package odyssey.backend.presentation.directory.dto.response;

import odyssey.backend.domain.directory.Directory;
import odyssey.backend.presentation.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

public record TeamDirectoryResponse(
        Long id,
        String name,
        Long teamId,
        List<SimpleRoadmapResponse> roadmaps
) {
    public static TeamDirectoryResponse from(Directory directory) {
        return new TeamDirectoryResponse(
                directory.getId(),
                directory.getName(),
                directory.getTeamId(),
                directory.getRoadmaps() != null
                        ? directory.getRoadmaps().stream()
                        .map(SimpleRoadmapResponse::from)
                        .toList()
                        : List.of()
        );
    }
}

