package odyssey.backend.application.auth;

import lombok.RequiredArgsConstructor;
import odyssey.backend.infrastructure.jwt.dto.response.TokenResponse;
import odyssey.backend.infrastructure.jwt.service.TokenService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshService {

    private final TokenService tokenService;

    public TokenResponse refreshToken(String refreshToken) {
        String accessToken = tokenService.refreshAccessToken(refreshToken);

        return TokenResponse.create(accessToken, refreshToken);
    }
}
