package odyssey.backend.roadmap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.image.domain.Image;
import odyssey.backend.image.service.ImageService;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@Component
public class RoadmapFacade {

    private final RoadmapRepository roadmapRepository;
    private final ImageService imageService;
    private final DirectoryService directoryService;

    @Transactional
    public RoadmapResponse save(RoadmapRequest request, MultipartFile thumbnail) {

        Directory directory = null;

        if (request.getDirectoryId() != null) {
            directory = directoryService.findDirectoryById(request.getDirectoryId());
        }

        Roadmap roadmap = roadmapRepository.save(
                Roadmap.from(request, directory)
        );

        roadmap.updateLastModifiedAt();

        Image image = imageService.save(thumbnail, roadmap);

        log.info("생성된 로드맵 Id : {}", roadmap.getId());

        return RoadmapResponse.from(roadmap, image.getUrl());
    }

    @Transactional
    public void deleteRoadmapById(Long id) {
        Roadmap roadmap = findRoadmapById(id);

        log.info("삭제된 로드맵 Id : {}", id);

        imageService.deleteImageByRoadmap(roadmap);

        roadmapRepository.deleteById(id);
    }

    private Roadmap findRoadmapById(Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(RoadmapNotFoundException::new);
    }

    @Transactional
    public RoadmapResponse update(Long id, RoadmapRequest request) {
        Roadmap roadmap = findRoadmapById(id);

        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        roadmap.updateLastModifiedAt();

        log.info("업데이트 요청 로드맵 Id : {}", roadmap.getId());

        Image image = imageService.getImageByRoadmap(roadmap);

        return RoadmapResponse.from(roadmap, image.getUrl());
    }

    @Transactional
    public RoadmapResponse toggleFavorite(Long id) {
        Roadmap roadmap = findRoadmapById(id);

        roadmap.toggleFavorite();

        log.info("즐겨찾기 요청 로드맵 Id : {}", roadmap.getId());

        return RoadmapResponse.from(roadmap, roadmap.getImage().getUrl());
    }

}
