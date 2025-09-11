package odyssey.backend.application.directory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.directory.exception.DirectoryNotFoundException;
import odyssey.backend.infrastructure.persistence.directory.DirectoryRepository;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.directory.dto.response.TeamDirectoryResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final SimpMessagingTemplate messagingTemplate;

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



    public void deleteDirectory(Long id) {
        directoryRepository.deleteById(id);
    }

    public Directory findDirectoryById(Long id) {
        return directoryRepository.findById(id)
                .orElseThrow(DirectoryNotFoundException::new);
    }

    public TeamDirectoryResponse createTeamDirectory(Long teamId, DirectoryRequest request, User user) {
        Directory parent = null;
        
        if (request.getParentId() != null) {
            parent = directoryRepository.findById(request.getParentId())
                    .orElseThrow(DirectoryNotFoundException::new);
        }
        
        Directory directory = Directory.fromTeam(request, parent, teamId);
        directoryRepository.save(directory);
        
        TeamDirectoryResponse response = TeamDirectoryResponse.from(directory);

        messagingTemplate.convertAndSend("/topic/directory/team/" + teamId + "/created", response);
        
        return response;
    }

    @Transactional
    public TeamDirectoryResponse updateTeamDirectory(Long id, Long teamId, DirectoryRequest request, User user) {
        Directory directory = findDirectoryById(id);
        
        Directory parent = null;
        if (request.getParentId() != null) {
            parent = findDirectoryById(request.getParentId());
        }
        
        directory.update(request.getName(), parent);
        TeamDirectoryResponse response = TeamDirectoryResponse.from(directory);
        
        messagingTemplate.convertAndSend("/topic/directory/team/" + teamId + "/updated", response);
        
        return response;
    }

    public void deleteTeamDirectory(Long id, Long teamId, User user) {

        messagingTemplate.convertAndSend("/topic/directory/team/" + teamId + "/deleted", id);
        
        directoryRepository.deleteById(id);
    }

}
