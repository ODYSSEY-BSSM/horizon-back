package odyssey.backend.application.directory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.infrastructure.persistence.directory.DirectoryRepository;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.directory.dto.response.RootContentResponse;
import odyssey.backend.domain.directory.exception.DirectoryNotFoundException;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
import odyssey.backend.presentation.roadmap.dto.response.SimpleRoadmapResponse;
import odyssey.backend.domain.auth.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final RoadmapRepository roadmapRepository;

    public DirectoryResponse createDirectory(DirectoryRequest directoryRequest, User user) {
        Directory parent = null;

        if (directoryRequest.getParentId() != null) {
            parent = directoryRepository.findById(directoryRequest.getParentId())
                    .orElseThrow(DirectoryNotFoundException::new);
        }

        Directory directory = Directory.from(directoryRequest, parent, user);

        directoryRepository.save(directory);

        return DirectoryResponse.from(directory);
    }

    @Transactional
    public DirectoryResponse updateDirectory(Long id, DirectoryRequest request, User user) {
        Directory directory = findDirectoryById(id);

        Directory parent = null;

        if (request.getParentId() != null) {
            parent = findDirectoryById(request.getParentId());
        }
        directory.update(request.getName(), parent);

        return DirectoryResponse.from(directory);
    }

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

    public void deleteDirectory(Long id) {
        directoryRepository.deleteById(id);
    }

    public Directory findDirectoryById(Long id) {
        return directoryRepository.findById(id)
                .orElseThrow(DirectoryNotFoundException::new);
    }

}
