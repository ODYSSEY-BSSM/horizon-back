package odyssey.backend.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.jwt.domain.RefreshToken;
import odyssey.backend.global.jwt.domain.RefreshTokenRepository;
import odyssey.backend.global.jwt.exception.TokenNotFoundException;
import odyssey.backend.user.domain.User;
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
