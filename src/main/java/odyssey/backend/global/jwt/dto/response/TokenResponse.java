package odyssey.backend.global.jwt.dto.response;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String accessToken;

    private final String refreshToken;

    public static TokenResponse create(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }

    TokenResponse(String access_token, String refresh_token) {
        this.accessToken = access_token;
        this.refreshToken = refresh_token;
    }
}
