package odyssey.backend.websocket.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import odyssey.backend.image.domain.Image;
import odyssey.backend.roadmap.domain.Roadmap;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapUpdateResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> categories;
    private String imageUrl;
    private Long userId;
    private String userName;

    public static RoadmapUpdateResponse from(Roadmap roadmap, Image image, Long userId, String userName) {
        return RoadmapUpdateResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .categories(roadmap.getCategories())
                .imageUrl(image.getUrl())
                .userId(userId)
                .userName(userName)
                .build();
    }
}