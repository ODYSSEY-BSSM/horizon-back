package odyssey.backend.directory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.domain.DirectoryRepository;
import odyssey.backend.directory.dto.DirectoryRequest;
import odyssey.backend.directory.dto.DirectoryResponse;
import odyssey.backend.directory.dto.RootContentResponse;
import odyssey.backend.global.exception.DirectoryNotFoundException;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.SimpleRoadmapResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final RoadmapRepository roadmapRepository;

    public DirectoryResponse createDirectory(DirectoryRequest directoryRequest) {
        Directory parent = null;

        if (directoryRequest.getParentId() != null) {
            parent = directoryRepository.findById(directoryRequest.getParentId())
                    .orElseThrow(DirectoryNotFoundException::new);
        }

        Directory directory = Directory.builder()
                .name(directoryRequest.getName())
                .parent(parent)
                .build();

        directoryRepository.save(directory);

        return new DirectoryResponse(directory);
    }

    @Transactional
    public DirectoryResponse updateDirectory(Long id, DirectoryRequest request) {
        Directory directory = findDirectoryById(id);

        Directory parent = null;

        if (request.getParentId() != null) {
            parent = findDirectoryById(request.getParentId());
        }
        directory.update(request.getName(), parent);

        return new DirectoryResponse(directory);
    }

    public RootContentResponse getRootContents() {
        List<Directory> rootDirectories = directoryRepository.findByParentIsNull();
        List<Roadmap> rootRoadmaps = roadmapRepository.findByDirectoryIsNull();

        List<DirectoryResponse> directoryResponses = rootDirectories.stream()
                .map(DirectoryResponse::new)
                .toList();

        List<SimpleRoadmapResponse> roadmapResponses = rootRoadmaps.stream()
                .map(SimpleRoadmapResponse::new)
                .toList();

        return new RootContentResponse(directoryResponses, roadmapResponses);
    }

    public void deleteDirectory(Long id) {
        directoryRepository.deleteById(id);
    }

    public Directory findDirectoryById(Long id) {
        return directoryRepository.findById(id)
                .orElseThrow(DirectoryNotFoundException::new);
    }

}
