package odyssey.backend.directory.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.domain.DirectoryRepository;
import odyssey.backend.directory.dto.DirectoryRequest;
import odyssey.backend.directory.dto.DirectoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

    public DirectoryResponse createDirectory(DirectoryRequest directoryRequest) {
        Directory parent = null;

        if (directoryRequest.getParentId() != null) {
            parent = directoryRepository.findById(directoryRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException(""));
        }

        Directory directory = Directory.builder()
                .name(directoryRequest.getName())
                .parent(parent)
                .build();

        directoryRepository.save(directory);

        return new DirectoryResponse(directory);
    }

    public List<DirectoryResponse> getAllDirectories() {
        return directoryRepository.findAll().stream()
                .filter(directory -> directory.getParent() == null)
                .map(DirectoryResponse::new)
                .toList();
    }

    public DirectoryResponse getDirectory(Long id) {
        return new DirectoryResponse(findDirectoryById(id));
    }


    public Directory findDirectoryById(Long id) {
        return directoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("디렉토리를 찾을 수 없습니다."));
    }

}
