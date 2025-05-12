package odyssey.backend.roadmap.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class RoadmapRequest {

    @NotBlank(message = "필수값입니다.")
    @Size(max = 64)
    private String title;

    @NotBlank(message = "필수값입니다.")
    @Size(max = 150)
    private String description;

}
