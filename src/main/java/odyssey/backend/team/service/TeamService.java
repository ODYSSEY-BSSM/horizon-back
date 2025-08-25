package odyssey.backend.team.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamRepository;
import odyssey.backend.team.dto.request.TeamRequest;
import odyssey.backend.team.dto.response.TeamResponse;
import odyssey.backend.user.domain.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamResponse create(TeamRequest request, User leader){
        return TeamResponse.from(teamRepository.save(
                Team.ok(request, leader.getUsername())
        ));
    }

}
