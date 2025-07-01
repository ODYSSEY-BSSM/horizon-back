package odyssey.backend.directory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.directory.dto.DirectoryRequest;
import odyssey.backend.directory.dto.DirectoryResponse;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("/create")
    public SingleCommonResponse<DirectoryResponse> createDirectory(
            @Valid @RequestBody DirectoryRequest request
    ){
        return CommonResponse.ok(directoryService.createDirectory(request));
    }

    @GetMapping("/getAll")
    public ListCommonResponse<DirectoryResponse> getAll() {
        return CommonResponse.ok(directoryService.getAllDirectories());
    }

    @GetMapping("/{id}")
    public SingleCommonResponse<DirectoryResponse> getDirectoryById(@PathVariable Long id) {
        return CommonResponse.ok(directoryService.getDirectory(id));
    }

}
