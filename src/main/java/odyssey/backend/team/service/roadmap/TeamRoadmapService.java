package odyssey.backend.team.service.roadmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.global.s3.S3Service;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamRepository;
import odyssey.backend.team.dto.response.TeamRoadmapResponse;
import odyssey.backend.websocket.dto.roadmap.RoadmapCreatedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapDeletedNotification;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdatedNotification;
import odyssey.backend.websocket.service.WebSocketSessionManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamRoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final TeamRepository teamRepository;
    private final DirectoryService directoryService;
    private final S3Service s3Service;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @Transactional
    public TeamRoadmapResponse createRoadmap(Long teamId, RoadmapRequest request, MultipartFile thumbnail) {
        Team team = findTeamById(teamId);

        Directory directory = null;

        if (request.getDirectoryId() != null) {
            directory = directoryService.findDirectoryById(request.getDirectoryId());
        }

        String imageUrl = s3Service.uploadFile(thumbnail);
        
        Roadmap roadmap = roadmapRepository.save(
            Roadmap.fromTeam(request, imageUrl, directory, team)
        );

        RoadmapCreatedNotification notification = RoadmapCreatedNotification.builder()
                        .id(roadmap.getId())
                        .title(roadmap.getTitle())
                        .description(roadmap.getDescription())
                        .categories(roadmap.getCategories())
                        .imageUrl(roadmap.getImageUrl())
                        .directoryId(roadmap.getDirectory().getId())
                        .build();

        sendNotificationToTeam(teamId, "created", notification);

        log.info("팀 로드맵 생성 완료 - 팀ID: {}, 로드맵ID: {}, 제목: {}", teamId, roadmap.getId(), roadmap.getTitle());
        return TeamRoadmapResponse.from(roadmap, teamId, team.getName());
    }

    @Transactional(readOnly = true)
    public List<TeamRoadmapResponse> getTeamRoadmaps(Long teamId) {
        Team team = findTeamById(teamId);

        return roadmapRepository.findByTeamOrderByLastAccessedAtDesc(team)
                .stream()
                .map(roadmap -> TeamRoadmapResponse.from(roadmap, teamId, team.getName()))
                .toList();
    }

    @Transactional
    public TeamRoadmapResponse updateRoadmap(Long teamId, Long roadmapId, RoadmapRequest request) {
        Team team = findTeamById(teamId);

        Roadmap roadmap = findRoadmapById(roadmapId);

        if(!roadmap.getDirectory().getId().equals(request.getDirectoryId())) {
            roadmap.changeDirectory(directoryService.findDirectoryById(request.getDirectoryId()));
            roadmap.updateLastModifiedAt();
        }
        
        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        RoadmapUpdatedNotification notification = RoadmapUpdatedNotification.builder()
                .roadmapId(roadmapId)
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .categories(roadmap.getCategories())
                .imageUrl(roadmap.getImageUrl())
                .directoryId(roadmap.getDirectory().getId())
                .build();

        sendNotificationToTeam(teamId, "updated", notification);

        log.info("팀 로드맵 수정 완료 - 팀ID: {}, 로드맵ID: {}, 제목: {}", teamId, roadmapId, roadmap.getTitle());
        return TeamRoadmapResponse.from(roadmap, teamId, team.getName());
    }

    @Transactional
    public void deleteRoadmap(Long teamId, Long roadmapId) {
        Roadmap roadmap = findRoadmapById(roadmapId);
        String roadmapTitle = roadmap.getTitle();
        
        roadmapRepository.delete(roadmap);

        RoadmapDeletedNotification notification = new RoadmapDeletedNotification(roadmapId);
        sendNotificationToTeam(teamId, "deleted", notification);

        log.info("팀 로드맵 삭제 완료 - 팀ID: {}, 로드맵ID: {}, 제목: {}", teamId, roadmapId, roadmapTitle);
    }

    @Transactional(readOnly = true)
    public TeamRoadmapResponse getRoadmap(Long teamId, Long roadmapId) {
        Team team = findTeamById(teamId);
        Roadmap roadmap = findRoadmapById(roadmapId);
        return TeamRoadmapResponse.from(roadmap, teamId, team.getName());
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    private Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadmapNotFoundException::new);
    }

    private void sendNotificationToTeam(Long teamId, String event, Object payload) {
        Set<String> subscribedUsers = sessionManager.getUsersSubscribedToTeam(teamId);
        for (String userId : subscribedUsers) {
            messagingTemplate.convertAndSendToUser(userId, "/topic/team/" + teamId + "/roadmap/" + event, payload);
        }
    }
}