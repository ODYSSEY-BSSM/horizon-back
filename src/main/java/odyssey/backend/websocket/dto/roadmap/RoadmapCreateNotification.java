package odyssey.backend.websocket.dto.roadmap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public class RoadmapCreateNotification {

    @NotNull(message = "id는 필수입니다.")
    private Long id;

    @NotBlank(message = "title은 필수입니다.")
    private String title;

    @NotBlank(message = "description은 필수입니다.")
    private String description;

    @NotEmpty(message = "categories는 필수입니다.")
    private List<String> categories;

    @NotBlank(message = "imageUrl은 필수입니다.")
    private String imageUrl;
}
