package odyssey.backend.application.roadmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.domain.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.domain.team.Team;
import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
import odyssey.backend.infrastructure.s3.S3Service;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.PersonalRoadmapResponse;
import odyssey.backend.presentation.roadmap.dto.response.TeamRoadmapResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@Component
public class RoadmapFacade {

    private final RoadmapRepository roadmapRepository;
    private final S3Service s3Service;
    private final DirectoryService directoryService;
    private final TeamService teamService;

    @Transactional
    public PersonalRoadmapResponse savePersonalRoadmap(RoadmapRequest request, MultipartFile thumbnail, User user){

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        String url = s3Service.uploadFile(thumbnail);

        Roadmap roadmap = roadmapRepository.save(
                Roadmap.from(request, url, directory, user, null)
        );

        roadmap.updateLastModifiedAt();

        log.info("생성된 로드맵 Id : {}", roadmap.getId());

        return PersonalRoadmapResponse.from(roadmap, user.getUuid());
    }

    @Transactional
    public TeamRoadmapResponse saveTeamRoadmap(RoadmapRequest request, MultipartFile thumbnail, User user, Long teamId){
        String url = s3Service.uploadFile(thumbnail);
        Team team = teamService.findByTeamId(teamId);

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        return TeamRoadmapResponse.from(roadmapRepository.save(
                Roadmap.from(request, url, directory, user, team)), user.getUuid());
    }

    @Transactional
    public void deleteRoadmapById(Long id) {
        Roadmap roadmap = findRoadmapById(id);

        log.info("삭제된 로드맵 Id : {}", id);

        s3Service.deleteFile(roadmap.getImageUrl());

        roadmapRepository.deleteById(id);
    }

    private Roadmap findRoadmapById(Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(RoadmapNotFoundException::new);
    }

    @Transactional
    public PersonalRoadmapResponse update(Long id, RoadmapRequest request, User user) {
        Roadmap roadmap = findRoadmapById(id);

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        if(!roadmap.getDirectory().getId().equals(directory.getId())) {
            roadmap.changeDirectory(directory);
            roadmap.updateLastModifiedAt();
        }

        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        return PersonalRoadmapResponse.from(roadmap, user.getUuid());
    }

    @Transactional
    public PersonalRoadmapResponse toggleFavorite(Long id, User user) {
        Roadmap roadmap = findRoadmapById(id);

        roadmap.toggleFavorite();

        log.info("즐겨찾기 요청 로드맵 Id : {}", roadmap.getId());

        return PersonalRoadmapResponse.from(roadmap, user.getUuid());
    }

}
