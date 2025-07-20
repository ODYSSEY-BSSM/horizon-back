package odyssey.backend.directory.dto.response;

import lombok.Getter;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

@Getter
public class RootContentResponse {
    private final List<DirectoryResponse> directories;
    private final List<SimpleRoadmapResponse> roadmaps;

    public static RootContentResponse from(List<DirectoryResponse> directories, List<SimpleRoadmapResponse> roadmaps) {
        return new RootContentResponse(directories, roadmaps);
    }

    RootContentResponse(List<DirectoryResponse> directories,
                        List<SimpleRoadmapResponse> roadmaps) {
        this.directories = directories;
        this.roadmaps = roadmaps;
    }
}