package odyssey.backend.presentation.roadmap.dto.response;

public record ImageUrlResponse(String url) {
    public static ImageUrlResponse create(String url) {
        return new ImageUrlResponse(url);
    }
}
