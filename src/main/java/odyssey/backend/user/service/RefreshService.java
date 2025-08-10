package odyssey.backend.user.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.global.jwt.dto.response.TokenResponse;
import odyssey.backend.global.jwt.service.TokenService;
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
