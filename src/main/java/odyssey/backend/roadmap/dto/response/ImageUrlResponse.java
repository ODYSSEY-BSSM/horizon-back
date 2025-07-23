package odyssey.backend.roadmap.dto.response;

import lombok.Getter;

@Getter
public class ImageUrlResponse {

    private final String url;

    public static ImageUrlResponse create(String url) {
        return new ImageUrlResponse(url);
    }

    ImageUrlResponse(String url) {
        this.url = url;
    }

}
