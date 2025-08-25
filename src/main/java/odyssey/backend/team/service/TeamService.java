package odyssey.backend.team.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamApplyRepository;
import odyssey.backend.team.domain.TeamRepository;
import odyssey.backend.team.dto.request.TeamRequest;
import odyssey.backend.team.dto.response.TeamResponse;
import odyssey.backend.user.domain.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamApplyRepository teamApplyRepository;

    public TeamResponse create(TeamRequest request, User leader){
        return TeamResponse.from(teamRepository.save(
                Team.ok(request, leader.getUsername())
        ));
    }

    public void delete(Long id, User user){
        Team team = teamRepository.findByLeader(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("팀은 팀장만 삭제할 수 있읍니다."));

        teamRepository.delete(team);
    }

    public TeamResponse findById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return TeamResponse.from(team);
    }

}
