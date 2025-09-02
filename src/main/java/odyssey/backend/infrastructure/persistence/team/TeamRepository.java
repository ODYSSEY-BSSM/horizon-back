package odyssey.backend.infrastructure.persistence.team;

import odyssey.backend.domain.team.Team;
import odyssey.backend.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByLeader(User leader);
}
