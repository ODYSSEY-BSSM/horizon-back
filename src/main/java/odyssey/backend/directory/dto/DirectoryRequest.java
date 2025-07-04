package odyssey.backend.directory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DirectoryRequest {

    @NotBlank
    @Size(max = 64, message = "필수입니다.")
    private String name;

    private Long parentId;

}
