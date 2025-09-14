package odyssey.backend.presentation.root.dto.response;

import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;

import java.util.List;

public record RootContentResponse(
        List<DirectoryResponse> directories
) {
    public static RootContentResponse from(List<DirectoryResponse> directories) {
        return new RootContentResponse(directories);
    }
}
