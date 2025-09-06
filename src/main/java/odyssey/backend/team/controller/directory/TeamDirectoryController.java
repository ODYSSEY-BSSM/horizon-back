package odyssey.backend.team.controller.directory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.dto.response.RootContentResponse;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.team.service.directory.TeamDirectoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/directories")
public class TeamDirectoryController {

    private final TeamDirectoryService teamDirectoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<DirectoryResponse> createTeamDirectory(
            @PathVariable Long teamId,
            @Valid @RequestBody DirectoryRequest request) {
        
        DirectoryResponse response = teamDirectoryService.createDirectory(teamId, request);
        return CommonResponse.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<DirectoryResponse> getTeamDirectories(
            @PathVariable Long teamId) {
        
        List<DirectoryResponse> directories = teamDirectoryService.getTeamDirectories(teamId);
        return CommonResponse.ok(directories);
    }

    @GetMapping("/{directoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> getTeamDirectory(
            @PathVariable Long teamId,
            @PathVariable Long directoryId) {
        
        DirectoryResponse response = teamDirectoryService.getDirectory(teamId, directoryId);
        return CommonResponse.ok(response);
    }

    @PutMapping("/{directoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateTeamDirectory(
            @PathVariable Long teamId,
            @PathVariable Long directoryId,
            @Valid @RequestBody DirectoryRequest request) {
        
        DirectoryResponse response = teamDirectoryService.updateDirectory(teamId, directoryId, request);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/{directoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteTeamDirectory(
            @PathVariable Long teamId,
            @PathVariable Long directoryId) {
        
        teamDirectoryService.deleteDirectory(teamId, directoryId);
        return CommonResponse.ok("팀 디렉토리가 삭제되었습니다.");
    }

    @GetMapping("/root-contents")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RootContentResponse> getTeamRootContents(
            @PathVariable Long teamId) {
        
        RootContentResponse response = teamDirectoryService.getRootContents(teamId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/{directoryId}/roadmaps")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RootContentResponse> getDirectoryRoadmaps(
            @PathVariable Long teamId,
            @PathVariable Long directoryId) {
        
        RootContentResponse response = teamDirectoryService.getDirectoryContents(teamId, directoryId);
        return CommonResponse.ok(response);
    }
}