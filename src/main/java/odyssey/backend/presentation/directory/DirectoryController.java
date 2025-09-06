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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateDirectory(
            @PathVariable Long id,
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(directoryService.updateDirectory(id, request, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SingleCommonResponse<String> deleteDirectory(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        directoryService.deleteDirectory(id);

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

    @PutMapping("/{id}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateTeamDirectory(
            @PathVariable Long id,
            @PathVariable Long teamId,
            @Valid @RequestBody DirectoryRequest request,
            @AuthenticationPrincipal User user) {
        DirectoryResponse response = directoryService.updateTeamDirectory(id, teamId, request, user);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/{id}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteTeamDirectory(
            @PathVariable Long id,
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user) {
        directoryService.deleteTeamDirectory(id, teamId, user);
        return CommonResponse.ok("팀 디렉토리가 삭제되었습니다.");
    }

}
