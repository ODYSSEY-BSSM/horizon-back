    package odyssey.backend.roadmap.service;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import odyssey.backend.directory.service.DirectoryService;
    import odyssey.backend.roadmap.dto.response.ImageUrlResponse;
    import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
    import odyssey.backend.image.domain.Image;
    import odyssey.backend.image.service.ImageService;
    import odyssey.backend.roadmap.domain.RoadmapRepository;
    import odyssey.backend.roadmap.dto.response.RoadmapCountResponse;
    import odyssey.backend.roadmap.dto.response.RoadmapResponse;
    import org.springframework.stereotype.Service;
    import odyssey.backend.roadmap.domain.Roadmap;

    import java.util.List;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class RoadmapService {

        private final RoadmapRepository roadmapRepository;
        private final ImageService imageService;
        private final DirectoryService directoryService;


        public List<RoadmapResponse> findAllRoadmaps() {
            return roadmapRepository.findAllByOrderByLastAccessedAtDesc().stream()
                    .map(roadmap -> {
                        Image image = imageService.getImageByRoadmap(roadmap);
                        return new RoadmapResponse(roadmap, image.getUrl());
                    })
                    .toList();
        }

        public RoadmapResponse getLastAccessedRoadmap() {

            Roadmap roadmap = roadmapRepository.findTopByOrderByLastAccessedAtDesc()
                    .orElseThrow(RoadmapNotFoundException::new);

            Image image = imageService.getImageByRoadmap(roadmap);

            log.info("마지막 접속 로드맵 Id : {}", roadmap.getId());

            return new RoadmapResponse(roadmap, image.getUrl());
        }

        public List<Roadmap> findByDirectoryIsNull() {
            return roadmapRepository.findByDirectoryIsNull();

        }

        public RoadmapCountResponse getRoadmapCount() {
            Long count = roadmapRepository.count();

            return new RoadmapCountResponse(count);
        }

        public ImageUrlResponse getUrlByRoadmapId(Long id) {
            Image image = imageService.getImageByRoadmapId(id);

            return new ImageUrlResponse(image.getUrl());
        }



    }
