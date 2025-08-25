package odyssey.backend.team.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.team.dto.request.TeamRequest;
import odyssey.backend.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "tbl_team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    private String leader;

    @ManyToMany
    private List<User> members = new ArrayList<>();

    Team(String name, String leader){
        this.name = name;
        this.leader = leader;
    }

    public static Team ok(TeamRequest request, String leader){
        return new Team(
                request.getName(),
                leader
        );
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public boolean isLeader(User user){
        return this.leader.equals(user.getUsername());
    }

}
