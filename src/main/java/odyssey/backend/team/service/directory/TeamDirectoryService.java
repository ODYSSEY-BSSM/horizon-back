package odyssey.backend.team.service.directory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.domain.DirectoryRepository;
import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.exception.DirectoryNotFoundException;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamDirectoryService {

    private final DirectoryRepository directoryRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public DirectoryResponse createDirectory(Long teamId, DirectoryRequest request) {
        Team team = findTeamById(teamId);
        
        Directory parentDirectory = null;
        if (request.getParentId() != null) {
            parentDirectory = findDirectoryById(request.getParentId());
            validateDirectoryBelongsToTeam(parentDirectory, teamId);
        }

        Directory directory = Directory.fromTeam(request, parentDirectory, team);
        Directory savedDirectory = directoryRepository.save(directory);

        log.info("팀 디렉토리 생성 완료 - 팀ID: {}, 디렉토리ID: {}, 이름: {}", 
                teamId, savedDirectory.getId(), savedDirectory.getName());
        
        return DirectoryResponse.from(savedDirectory);
    }

    @Transactional(readOnly = true)
    public List<DirectoryResponse> getTeamDirectories(Long teamId) {
        Team team = findTeamById(teamId);
        
        List<Directory> directories = directoryRepository.findByParentIsNullAndTeam(team);
        return directories.stream()
                .map(DirectoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DirectoryResponse getDirectory(Long teamId, Long directoryId) {
        Directory directory = findDirectoryById(directoryId);
        validateDirectoryBelongsToTeam(directory, teamId);
        
        return DirectoryResponse.from(directory);
    }

    @Transactional
    public DirectoryResponse updateDirectory(Long teamId, Long directoryId, DirectoryRequest request) {
        Directory directory = findDirectoryById(directoryId);
        validateDirectoryBelongsToTeam(directory, teamId);

        Directory newParent = null;
        if (request.getParentId() != null) {
            newParent = findDirectoryById(request.getParentId());
            validateDirectoryBelongsToTeam(newParent, teamId);
        }

        directory.update(request.getName(), newParent);

        log.info("팀 디렉토리 수정 완료 - 팀ID: {}, 디렉토리ID: {}, 이름: {}", 
                teamId, directoryId, directory.getName());
        
        return DirectoryResponse.from(directory);
    }

    @Transactional
    public void deleteDirectory(Long teamId, Long directoryId) {
        Directory directory = findDirectoryById(directoryId);
        validateDirectoryBelongsToTeam(directory, teamId);
        
        String directoryName = directory.getName();
        directoryRepository.delete(directory);

        log.info("팀 디렉토리 삭제 완료 - 팀ID: {}, 디렉토리ID: {}, 이름: {}", 
                teamId, directoryId, directoryName);
    }


    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    private Directory findDirectoryById(Long directoryId) {
        return directoryRepository.findById(directoryId)
                .orElseThrow(DirectoryNotFoundException::new);
    }

    private void validateDirectoryBelongsToTeam(Directory directory, Long teamId) {
        if (directory.getTeam() == null || !directory.getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("해당 디렉토리는 이 팀에 속하지 않습니다.");
        }
    }
}