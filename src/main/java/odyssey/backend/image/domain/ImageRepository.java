package odyssey.backend.image.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByRoadmapId(Long roadmapId);
    void deleteByRoadmapId(Long roadmapId);
}
