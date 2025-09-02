package odyssey.backend.presentation.directory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.directory.dto.response.RootContentResponse;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import odyssey.backend.domain.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("/create")
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

    @GetMapping("/root-contents")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RootContentResponse> getRootContents(
            @AuthenticationPrincipal User user
    ) {
        RootContentResponse response = directoryService.getRootContents(user);
        return CommonResponse.ok(response);
    }
}
