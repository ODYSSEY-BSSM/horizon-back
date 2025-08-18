package odyssey.backend.directory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.domain.DirectoryRepository;
import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.dto.response.RootContentResponse;
import odyssey.backend.directory.exception.DirectoryNotFoundException;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;
import odyssey.backend.user.domain.User;
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
