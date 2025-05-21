package odyssey.backend.image.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByRoadmapId(Long roadmapId);
}
