package odyssey.backend.team.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.team.domain.Team;
import odyssey.backend.team.domain.TeamApply;
import odyssey.backend.team.domain.TeamApplyRepository;
import odyssey.backend.team.domain.TeamRepository;
import odyssey.backend.team.dto.response.ApplyResponse;
import odyssey.backend.user.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamApplyService {

    private final TeamRepository teamRepository;
    private final TeamApplyRepository teamApplyRepository;

    public ApplyResponse apply(Long teamId, User user){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        return ApplyResponse.of(teamApplyRepository.save(new TeamApply(team, user)));
    }

    @Transactional
    public ApplyResponse approve(Long applyId, User user){
        TeamApply apply = teamApplyRepository.findById(applyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청입니다"));

        Team team = apply.getTeam();

        if(!team.isLeader(user)){
            throw new IllegalArgumentException("팀장만 신청을 관리할 수 있습니다.");
        }

        team.addMember(apply.getUser());

        return ApplyResponse.of(teamApplyRepository.save(apply));
    }

    @Transactional
    public void reject(Long applyId, User user){
        TeamApply apply = teamApplyRepository.findById(applyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청입니다"));

        Team team = apply.getTeam();

        if(!team.isLeader(user)){
            throw new IllegalArgumentException("팀장만 신청을 관리할 수 있습니다.");
        }

        teamApplyRepository.deleteById(applyId);
    }
}
