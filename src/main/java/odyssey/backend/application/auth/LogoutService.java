package odyssey.backend.application.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.infrastructure.jwt.domain.RefreshToken;
import odyssey.backend.infrastructure.jwt.domain.RefreshTokenRepository;
import odyssey.backend.infrastructure.jwt.exception.TokenNotFoundException;
import odyssey.backend.domain.auth.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(User user) {
        RefreshToken token = refreshTokenRepository.findByUuid(user.getUuid())
                .orElseThrow(TokenNotFoundException::new);

        refreshTokenRepository.delete(token);
    }
}
