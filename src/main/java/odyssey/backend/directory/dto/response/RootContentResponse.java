package odyssey.backend.directory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class RootContentResponse {
    private List<DirectoryResponse> directories;
    private List<SimpleRoadmapResponse> roadmaps;
}