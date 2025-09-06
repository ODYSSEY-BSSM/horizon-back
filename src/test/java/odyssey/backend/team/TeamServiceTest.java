package odyssey.backend.team;

import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.Role;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.team.Team;
import odyssey.backend.infrastructure.persistence.team.TeamApplyRepository;
import odyssey.backend.infrastructure.persistence.team.TeamRepository;
import odyssey.backend.presentation.team.dto.request.TeamRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamApplyRepository teamApplyRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void 팀장은_팀_멤버로_인식된다() {
        // given
        User leader = createUser(1L, "leader");
        User member = createUser(2L, "member");
        Team team = createTeam(1L, "Test Team", leader, List.of(leader, member));
        
        given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        // when
        boolean result = teamService.isUserMemberOfTeam(1L, 1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 팀_멤버는_팀_멤버로_인식된다() {
        // given
        User leader = createUser(1L, "leader");
        User member = createUser(2L, "member");
        Team team = createTeam(1L, "Test Team", leader, List.of(leader, member));
        
        given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        // when
        boolean result = teamService.isUserMemberOfTeam(2L, 1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 팀_멤버가_아닌_사용자는_팀_멤버로_인식되지_않는다() {
        // given
        User leader = createUser(1L, "leader");
        User member = createUser(2L, "member");
        User nonMember = createUser(3L, "nonMember");
        Team team = createTeam(1L, "Test Team", leader, List.of(leader, member));
        
        given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        // when
        boolean result = teamService.isUserMemberOfTeam(3L, 1L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 존재하지_않는_팀의_경우_false를_반환한다() {
        // given
        given(teamRepository.findById(999L)).willReturn(Optional.empty());

        // when
        boolean result = teamService.isUserMemberOfTeam(1L, 999L);

        // then
        assertThat(result).isFalse();
    }

    private User createUser(Long uuid, String username) {
        User user = new User("test@example.com", username, "password", Role.USER);
        try {
            var uuidField = user.getClass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(user, uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private Team createTeam(Long id, String name, User leader, List<User> members) {
        Team team = Team.ok(new TeamRequest(name), leader);
        
        for (User member : members) {
            if (!member.equals(leader)) {  // 팀장은 이미 추가되어 있음
                team.addMember(member);
            }
        }
        
        return team;
    }
}