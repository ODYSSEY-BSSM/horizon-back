package odyssey.backend.application.root;

import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.infrastructure.persistence.directory.DirectoryRepository;
import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.roadmap.dto.response.SimpleRoadmapResponse;
import odyssey.backend.presentation.root.dto.response.RootContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RootUseCase {

    private final DirectoryRepository directoryRepository;
    private final RoadmapRepository roadmapRepository;

    public RootContentResponse getRootContents(User user) {
        List<Directory> rootDirectories = directoryRepository.findByParentIsNullAndUser(user);
        List<Roadmap> rootRoadmaps = roadmapRepository.findByDirectoryIsNullAndUser(user);

        List<DirectoryResponse> directoryResponses = rootDirectories.stream()
                .map(DirectoryResponse::from)
                .toList();

        List<SimpleRoadmapResponse> roadmapResponses = rootRoadmaps.stream()
                .map(SimpleRoadmapResponse::from)
                .toList();

        return RootContentResponse.from(directoryResponses, roadmapResponses);
    }

    public RootContentResponse getTeamRootContents(User user, Long teamId) {
        List<Directory> rootDirectories = directoryRepository.findByParentIsNullAndTeamId(teamId);
        List<Roadmap> rootRoadmaps = roadmapRepository.findByDirectoryIsNullAndTeamId(teamId);

        List<DirectoryResponse> directoryResponses = rootDirectories.stream()
                .map(DirectoryResponse::from)
                .toList();

        List<SimpleRoadmapResponse> roadmapResponses = rootRoadmaps.stream()
                .map(SimpleRoadmapResponse::from)
                .toList();

        return RootContentResponse.from(directoryResponses, roadmapResponses);
    }

}
