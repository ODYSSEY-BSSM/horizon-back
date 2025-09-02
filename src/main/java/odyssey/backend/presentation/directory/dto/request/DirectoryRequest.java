package odyssey.backend.presentation.directory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DirectoryRequest {

    @NotBlank(message = "필수입니다.")
    @Size(max = 64)
    private String name;

    private Long parentId;

}
