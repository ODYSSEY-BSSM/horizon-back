package odyssey.backend.directory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.dto.DirectoryRequest;
import odyssey.backend.directory.dto.DirectoryResponse;
import odyssey.backend.directory.dto.RootContentResponse;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<DirectoryResponse> createDirectory(
            @Valid @RequestBody DirectoryRequest request
    ){
        return CommonResponse.ok(directoryService.createDirectory(request));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<DirectoryResponse> updateDirectory(
            @PathVariable Long id,
            @Valid @RequestBody DirectoryRequest request){
        return CommonResponse.ok(directoryService.updateDirectory(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SingleCommonResponse<String> deleteDirectory(@PathVariable Long id) {
        directoryService.deleteDirectory(id);

        return CommonResponse.ok("삭제되었습니다.");
    }

    @GetMapping("/root-contents")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RootContentResponse> getRootContents() {
        RootContentResponse response = directoryService.getRootContents();
        return CommonResponse.ok(response);
    }
}
