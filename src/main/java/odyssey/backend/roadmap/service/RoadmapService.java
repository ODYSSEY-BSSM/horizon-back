package odyssey.backend.roadmap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.global.exception.RoadmapNotFoundException;
import odyssey.backend.image.domain.Image;
import odyssey.backend.image.domain.ImageRepository;
import odyssey.backend.image.service.ImageService;
import odyssey.backend.node.domain.NodeRepository;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
import org.springframework.stereotype.Service;
import odyssey.backend.roadmap.domain.Roadmap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final NodeRepository nodeRepository;

    public List<RoadmapResponse> findAllRoadmaps() {
        return roadmapRepository.findAll().stream()
                .map(roadmap -> {
                    Image image = imageRepository.findByRoadmapId(roadmap.getId());
                    return new RoadmapResponse(roadmap, image.getUrl());
                })
                .toList();
    }

    @Transactional
    public RoadmapResponse save(RoadmapRequest request, MultipartFile thumbnail) {
        Roadmap roadmap = roadmapRepository.save(
                Roadmap.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .categories(request.getCategories())
                        .build()
        );

        Image image = imageService.save(thumbnail, roadmap);

        log.info("생성된 로드맵 제목 - {}", roadmap.getTitle());

        return new RoadmapResponse(roadmap, image.getUrl());
    }

    @Transactional
    public void deleteRoadmapById(Long id) {

        log.info("삭제된 로드맵 Id {}", id);

        roadmapRepository.deleteById(id);

    }

    @Transactional
    public RoadmapResponse update(Long id, RoadmapRequest request) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(RoadmapNotFoundException::new);

        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        log.info("업데이트 요청 로드맵 Id : {}", roadmap.getId());

        Image image = imageRepository.findByRoadmapId(roadmap.getId());
        return new RoadmapResponse(roadmap, image.getUrl());
    }

    @Transactional
    public RoadmapResponse toggleFavorite(Long id){
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(RoadmapNotFoundException::new);

        roadmap.toggleFavorite();

        log.info("즐겨찾기 요청 로드맵 Id : {}", roadmap.getId());

        Image image = imageRepository.findByRoadmapId(roadmap.getId());

        return new RoadmapResponse(roadmap, image.getUrl());
    }

    public RoadmapResponse getLastAccessedRoadmap() {

        Roadmap roadmap = roadmapRepository.findTopByOrderByLastAccessedAtDesc()
                .orElseThrow(RoadmapNotFoundException::new);

        Image image = imageRepository.findByRoadmapId(roadmap.getId());

        log.info("마지막 접속 로드맵 Id : {}", roadmap.getId());

        return new RoadmapResponse(roadmap, image.getUrl());
    }

    public RoadmapCountResponse getRoadmapCount(){
        Long count = roadmapRepository.count();

        return new RoadmapCountResponse(count);
    }

}
