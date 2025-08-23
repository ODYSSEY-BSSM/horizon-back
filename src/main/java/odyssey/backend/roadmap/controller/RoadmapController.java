package odyssey.backend.roadmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.roadmap.dto.response.ImageUrlResponse;
import odyssey.backend.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.roadmap.service.RoadmapFacade;
import odyssey.backend.roadmap.service.RoadmapService;
import odyssey.backend.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final RoadmapFacade roadmapFacade;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<RoadmapResponse> getAllRoadmaps(
            @AuthenticationPrincipal User user
    ) {
        List<RoadmapResponse> roadmaps = roadmapService.findAllRoadmaps(user);
        return CommonResponse.ok(roadmaps);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public SingleCommonResponse<RoadmapResponse> createRoadmap(
            @RequestPart("roadmap") @Valid RoadmapRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(roadmapFacade.save(request, thumbnail, user));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> updateRoadmap(
            @PathVariable Long id,
            @RequestBody @Valid RoadmapRequest request,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapFacade.update(id, request, user));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteRoadmap(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        roadmapFacade.deleteRoadmapById(id);

        return CommonResponse.ok("삭제되었습니다.");
    }

    @PostMapping("/favorite/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> toggleFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapFacade.toggleFavorite(id, user));
    }

    @GetMapping("/last-accessed")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> getLastAccessedRoadmap(
            @AuthenticationPrincipal User user
    ) {
        return CommonResponse.ok(roadmapService.getLastAccessedRoadmap(user));
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapCountResponse> getRoadmapCount(
            @AuthenticationPrincipal User user
    ) {
        return CommonResponse.ok(roadmapService.getRoadmapCount(user));
    }

    @GetMapping("/{id}/url")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<ImageUrlResponse> getThumbnailUrl(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapService.getUrlByRoadmapId(id));
    }
}
