package odyssey.backend.application.roadmap;

import lombok.RequiredArgsConstructor;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.domain.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.domain.team.Team;
import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.PersonalRoadmapResponse;
import odyssey.backend.presentation.roadmap.dto.response.TeamRoadmapResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class RoadmapFacade {

    private final RoadmapRepository roadmapRepository;
    private final DirectoryService directoryService;
    private final TeamService teamService;

    @Transactional
    public PersonalRoadmapResponse savePersonalRoadmap(RoadmapRequest request, User user){

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        Roadmap roadmap = roadmapRepository.save(
                Roadmap.from(request, directory, user, null)
        );

        roadmap.updateLastModifiedAt();

        return PersonalRoadmapResponse.from(roadmap, user.getUuid());
    }

    @Transactional
    public TeamRoadmapResponse saveTeamRoadmap(RoadmapRequest request, User user, Long teamId){
        Team team = teamService.findByTeamId(teamId);

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        return TeamRoadmapResponse.from(roadmapRepository.save(
                Roadmap.from(request, directory, user, team)), user.getUuid());
    }

    @Transactional
    public void deleteRoadmapById(Long id) {
        Roadmap roadmap = findRoadmapById(id);

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

        return PersonalRoadmapResponse.from(roadmap, user.getUuid());
    }

}
