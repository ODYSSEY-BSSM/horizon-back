package odyssey.backend.team.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.user.domain.User;

@Entity
@NoArgsConstructor
@Getter
public class TeamApply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public TeamApply(Team team, User user) {
        this.team = team;
        this.user = user;
        this.status = Status.SUBMITTED;
    }

    public enum Status {
        SUBMITTED,
        APPROVED
    }
}

