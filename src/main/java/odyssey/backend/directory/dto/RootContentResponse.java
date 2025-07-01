package odyssey.backend.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import odyssey.backend.roadmap.dto.SimpleRoadmapResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class RootContentResponse {
    private List<DirectoryResponse> directories;
    private List<SimpleRoadmapResponse> roadmaps;
}