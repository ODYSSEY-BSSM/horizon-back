package odyssey.backend.team.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.domain.DirectoryRepository;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.dto.response.RootContentResponse;
import odyssey.backend.directory.exception.DirectoryNotFoundException;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamRepository;
import odyssey.backend.team.service.directory.TeamDirectoryService;
import odyssey.backend.team.service.roadmap.TeamRoadmapService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamContentFacade {

    private final TeamDirectoryService teamDirectoryService;
    private final TeamRoadmapService teamRoadmapService;
    private final DirectoryRepository directoryRepository;
    private final RoadmapRepository roadmapRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public RootContentResponse getTeamRootContents(Long teamId) {
        Team team = findTeamById(teamId);
        
        // 팀의 루트 디렉토리들 조회
        List<Directory> directories = directoryRepository.findByParentIsNullAndTeam(team);
        List<DirectoryResponse> directoryResponses = directories.stream()
                .map(DirectoryResponse::from)
                .toList();

        // 팀의 루트 로드맵들 조회 (디렉토리에 속하지 않은 것들)
        List<Roadmap> roadmaps = roadmapRepository.findByDirectoryIsNullAndTeam(team);
        List<SimpleRoadmapResponse> roadmapResponses = roadmaps.stream()
                .map(SimpleRoadmapResponse::from)
                .toList();

        log.info("팀 루트 콘텐츠 조회 완료 - 팀ID: {}, 디렉토리: {}개, 로드맵: {}개", 
                teamId, directoryResponses.size(), roadmapResponses.size());
        
        return new RootContentResponse(directoryResponses, roadmapResponses);
    }

    @Transactional(readOnly = true)
    public RootContentResponse getDirectoryContents(Long teamId, Long directoryId) {
        validateTeamExists(teamId);
        Directory directory = findDirectoryById(directoryId);
        validateDirectoryBelongsToTeam(directory, teamId);

        // 해당 디렉토리의 하위 디렉토리들
        List<Directory> childDirectories = directory.getChildren();
        List<DirectoryResponse> directoryResponses = childDirectories.stream()
                .map(DirectoryResponse::from)
                .toList();

        // 해당 디렉토리에 속한 로드맵들
        List<Roadmap> roadmaps = findRoadmapsByDirectory(directory);
        List<SimpleRoadmapResponse> roadmapResponses = roadmaps.stream()
                .map(SimpleRoadmapResponse::from)
                .toList();

        log.info("디렉토리 콘텐츠 조회 완료 - 팀ID: {}, 디렉토리ID: {}, 하위디렉토리: {}개, 로드맵: {}개", 
                teamId, directoryId, directoryResponses.size(), roadmapResponses.size());

        return new RootContentResponse(directoryResponses, roadmapResponses);
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    private void validateTeamExists(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }
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

    private List<Roadmap> findRoadmapsByDirectory(Directory directory) {
        // Directory와 연관된 로드맵들을 조회하는 로직
        // 현재는 Repository에 해당 메소드가 없으므로 임시로 빈 리스트 반환
        // TODO: RoadmapRepository.findByDirectory(Directory directory) 메소드 추가 필요
        return directory.getRoadmaps(); // Directory 엔티티에 roadmaps 관계가 있다면
    }
}