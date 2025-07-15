package odyssey.backend.roadmap.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Optional<Roadmap> findTopByOrderByLastAccessedAtDesc();
    List<Roadmap> findByDirectoryIsNull();
    List<Roadmap> findAllByOrderByLastAccessedAtDesc();
}
