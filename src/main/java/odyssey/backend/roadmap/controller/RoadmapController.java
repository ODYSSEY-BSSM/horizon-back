package odyssey.backend.roadmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
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
    public ListCommonResponse<RoadmapResponse> getAllRoadmaps() {
        List<RoadmapResponse> roadmaps = roadmapService.findAllRoadmaps();
        return CommonResponse.ok(roadmaps);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleCommonResponse<RoadmapResponse> createRoadmap(
            @RequestPart("roadmap") @Valid RoadmapRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) {

        return CommonResponse.ok(roadmapService.save(request, thumbnail));
    }

    @PutMapping("/update/{id}")
    public SingleCommonResponse<RoadmapResponse> updateRoadmap(@PathVariable Long id, @RequestBody @Valid RoadmapRequest request) {
        return CommonResponse.ok(roadmapService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public SingleCommonResponse<String> deleteRoadmap(@PathVariable Long id) {
        roadmapService.deleteRoadmapById(id);

        return CommonResponse.ok("삭제되었습니다.");
    }

    @PostMapping("/favorite/{id}")
    public SingleCommonResponse<RoadmapResponse> toggleFavorite(@PathVariable Long id) {
        return CommonResponse.ok(roadmapService.toggleFavorite(id));
    }

    @GetMapping("/last-accessed")
    public SingleCommonResponse<RoadmapResponse> getLastAccessedRoadmap() {
        return CommonResponse.ok(roadmapService.getLastAccessedRoadmap());
    }

    @GetMapping("/count")
    public SingleCommonResponse<RoadmapCountResponse> getRoadmapCount() {
        return CommonResponse.ok(roadmapService.getRoadmapCount());
    }
}
