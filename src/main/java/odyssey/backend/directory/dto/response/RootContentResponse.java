package odyssey.backend.directory.dto.response;

import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

public record RootContentResponse(
        List<DirectoryResponse> directories,
        List<SimpleRoadmapResponse> roadmaps
) {
    public static RootContentResponse from(List<DirectoryResponse> directories, List<SimpleRoadmapResponse> roadmaps) {
        return new RootContentResponse(directories, roadmaps);
    }
}
