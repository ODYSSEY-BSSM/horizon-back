    package odyssey.backend.roadmap.service;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import odyssey.backend.global.s3.S3Service;
    import odyssey.backend.roadmap.dto.response.ImageUrlResponse;
    import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
    import odyssey.backend.roadmap.domain.RoadmapRepository;
    import odyssey.backend.roadmap.dto.response.RoadmapCountResponse;
    import odyssey.backend.roadmap.dto.response.RoadmapResponse;
    import odyssey.backend.user.domain.User;
    import org.springframework.stereotype.Service;
    import odyssey.backend.roadmap.domain.Roadmap;

    import java.util.List;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class RoadmapService {

        private final RoadmapRepository roadmapRepository;
        private final S3Service s3Service;


        public List<RoadmapResponse> findAllRoadmaps(User user) {
            return roadmapRepository.findByUserOrderByLastAccessedAtDesc(user).stream()
                    .map(roadmap -> RoadmapResponse.from(roadmap, user.getUuid()))
                    .toList();
        }

        public RoadmapResponse getLastAccessedRoadmap(User user) {

            Roadmap roadmap = roadmapRepository.findTopByUserOrderByLastAccessedAtDesc(user)
                    .orElseThrow(RoadmapNotFoundException::new);

            log.info("마지막 접속 로드맵 Id : {}", roadmap.getId());

            return RoadmapResponse.from(roadmap, user.getUuid());
        }

        public RoadmapCountResponse getRoadmapCount(User user) {
            Long count = roadmapRepository.countByUser(user);

            return RoadmapCountResponse.from(count);
        }

        public ImageUrlResponse getUrlByRoadmapId(Long id) {
            Roadmap roadmap = roadmapRepository.findById(id)
                    .orElseThrow(RoadmapNotFoundException::new);

            return ImageUrlResponse.create(roadmap.getImageUrl());
        }

    }
