package odyssey.backend.presentation.directory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<DirectoryResponse> createDirectory(
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(directoryService.createDirectory(request, user));
    }

    @PutMapping("/{directoryId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateDirectory(
            @PathVariable Long directoryId,
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(directoryService.updateDirectory(directoryId, request, user));
    }

    @DeleteMapping("/{directoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SingleCommonResponse<String> deleteDirectory(
            @PathVariable Long directoryId,
            @AuthenticationPrincipal User user) {
        directoryService.deleteDirectory(directoryId);

        return CommonResponse.ok("삭제되었습니다.");
    }

    @PostMapping("/team/{teamId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<DirectoryResponse> createTeamDirectory(
            @PathVariable Long teamId,
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user) {
        DirectoryResponse response = directoryService.createTeamDirectory(teamId, request, user);
        return CommonResponse.ok(response);
    }

    @PutMapping("/{directoryId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateTeamDirectory(
            @PathVariable Long directoryId,
            @PathVariable Long teamId,
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user) {
        DirectoryResponse response = directoryService.updateTeamDirectory(directoryId, teamId, request, user);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/{directoryId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteTeamDirectory(
            @PathVariable Long directoryId,
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user) {
        directoryService.deleteTeamDirectory(directoryId, teamId, user);
        return CommonResponse.ok("팀 디렉토리가 삭제되었습니다.");
    }

}
