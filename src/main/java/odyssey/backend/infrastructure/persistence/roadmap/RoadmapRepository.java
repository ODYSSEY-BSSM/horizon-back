package odyssey.backend.infrastructure.persistence.roadmap;

import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Optional<Roadmap> findTopByUserOrderByLastAccessedAtDesc(User user);
    List<Roadmap> findByUserAndTeamIsNullOrderByLastAccessedAtDesc(User user);
    List<Roadmap> findByTeam_IdOrderByLastAccessedAtDesc(Long teamId);
    Long countByUser(User user);
}
