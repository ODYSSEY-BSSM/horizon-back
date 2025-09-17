package odyssey.backend.application.root;

import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.infrastructure.persistence.directory.DirectoryRepository;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.root.dto.response.RootContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RootUseCase {

    private final DirectoryRepository directoryRepository;

    public RootContentResponse getRootContents(User user) {
        List<Directory> rootDirectories = directoryRepository.findByParentIsNullAndUser(user);

        List<DirectoryResponse> directoryResponses = rootDirectories.stream()
                .map(DirectoryResponse::from)
                .toList();

        return RootContentResponse.from(directoryResponses);
    }

    public RootContentResponse getTeamRootContents(User user, Long teamId) {
        List<Directory> rootDirectories = directoryRepository.findByParentIsNullAndTeamId(teamId);

        List<DirectoryResponse> directoryResponses = rootDirectories.stream()
                .map(DirectoryResponse::from)
                .toList();

        return RootContentResponse.from(directoryResponses);
    }

}
