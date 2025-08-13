package odyssey.backend.websocket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import odyssey.backend.websocket.domain.RoadmapActionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapCrudMessage {

    @NotNull(message = "액션 타입은 필수입니다.")
    private RoadmapActionType action;

    @NotNull(message = "로드맵 ID는 필수입니다.")
    private Long roadmapId;

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    private String title;

    @NotBlank(message = "설명은 비어있을 수 없습니다.")
    private String description;

    @NotEmpty(message = "카테고리는 비어있을 수 없습니다.")
    private List<String> categories;

    @NotBlank(message = "이미지 URL은 비어있을 수 없습니다.")
    private String imageUrl;

    private boolean isFavorite;

    private LocalDate lastModifiedAt;

    private LocalDateTime lastAccessedAt;

    private Long userId;

    private String userName;
}
