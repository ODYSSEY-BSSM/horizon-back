package odyssey.backend.websocket.dto.roadmap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapUpdateMessage {
    @NotNull(message = "로드맵 ID는 필수입니다.")
    private Long roadmapId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @NotEmpty(message = "카테고리는 하나 이상이어야 합니다.")
    private List<String> categories;

    private Long directoryId;
}
