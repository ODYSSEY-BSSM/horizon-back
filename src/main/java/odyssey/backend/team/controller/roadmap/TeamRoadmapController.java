package odyssey.backend.team.controller.roadmap;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.team.dto.response.TeamRoadmapResponse;
import odyssey.backend.team.service.roadmap.TeamRoadmapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/roadmaps")
public class TeamRoadmapController {

    private final TeamRoadmapService teamRoadmapService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<TeamRoadmapResponse> createTeamRoadmap(
            @PathVariable Long teamId,
            @RequestPart("roadmap") @Valid RoadmapRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail) {
        
        TeamRoadmapResponse response = teamRoadmapService.createRoadmap(teamId, request, thumbnail);
        return CommonResponse.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<TeamRoadmapResponse> getTeamRoadmaps(
            @PathVariable Long teamId) {
        
        List<TeamRoadmapResponse> roadmaps = teamRoadmapService.getTeamRoadmaps(teamId);
        return CommonResponse.ok(roadmaps);
    }

    @PutMapping("/{roadmapId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<TeamRoadmapResponse> updateTeamRoadmap(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @RequestBody @Valid RoadmapRequest request) {
        
        TeamRoadmapResponse response = teamRoadmapService.updateRoadmap(teamId, roadmapId, request);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/{roadmapId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteTeamRoadmap(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId) {
        
        teamRoadmapService.deleteRoadmap(teamId, roadmapId);
        return CommonResponse.ok("팀 로드맵이 삭제되었습니다.");
    }

    @GetMapping("/{roadmapId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<TeamRoadmapResponse> getTeamRoadmap(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId) {
        
        TeamRoadmapResponse response = teamRoadmapService.getRoadmap(teamId, roadmapId);
        return CommonResponse.ok(response);
    }
}