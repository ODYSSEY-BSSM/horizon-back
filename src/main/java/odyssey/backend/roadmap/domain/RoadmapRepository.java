package odyssey.backend.roadmap.domain;

import odyssey.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Optional<Roadmap> findTopByUserOrderByLastAccessedAtDesc(User user);
    List<Roadmap> findByDirectoryIsNullAndUser(User user);
    List<Roadmap> findByUserOrderByLastAccessedAtDesc(User user);
    Long countByUser(User user);
}
