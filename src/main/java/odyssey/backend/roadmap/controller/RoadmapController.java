package odyssey.backend.roadmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.roadmap.dto.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
import odyssey.backend.roadmap.service.RoadmapService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @GetMapping("/all")
    public List<RoadmapResponse> getAllRoadmaps() {
        return roadmapService.findAllRoadmaps();
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RoadmapResponse createRoadmap(
            @RequestPart("roadmap") @Valid RoadmapRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) {

        return roadmapService.save(request, thumbnail);
    }

    @PutMapping("/update/{id}")
    public RoadmapResponse updateRoadmap(@PathVariable Long id, @RequestBody @Valid RoadmapRequest request) {
        return roadmapService.update(id, request);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteRoadmap(@PathVariable Long id) {
        roadmapService.deleteRoadmapById(id);
    }

    @PostMapping("/favorite/{id}")
    public RoadmapResponse toggleFavorite(@PathVariable Long id) {
        return roadmapService.toggleFavorite(id);
    }

    @GetMapping("/last-accessed")
    public RoadmapResponse getLastAccessedRoadmap() {
        return roadmapService.getLastAccessedRoadmap();
    }

    @GetMapping("/count")
    public RoadmapCountResponse getRoadmapCount() {
        return roadmapService.getRoadmapCount();
    }

}
