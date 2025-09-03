package odyssey.backend.presentation.roadmap.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoadmapRequest {

    @NotBlank(message = "필수값입니다.")
    @Size(max = 64)
    private String title;

    @NotBlank(message = "필수값입니다.")
    @Size(max = 150)
    private String description;

    @NotEmpty(message = "필수값입니다.")
    private List<String> categories;

    private Long directoryId;

    private Long teamId;

}
