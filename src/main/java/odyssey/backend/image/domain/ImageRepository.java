package odyssey.backend.image.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByRoadmapId(Long roadmapId);
    void deleteByRoadmapId(Long roadmapId);
}
