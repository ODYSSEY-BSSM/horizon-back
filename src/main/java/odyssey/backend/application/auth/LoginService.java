package odyssey.backend.application.auth;

import lombok.RequiredArgsConstructor;
import odyssey.backend.infrastructure.jwt.dto.response.TokenResponse;
import odyssey.backend.infrastructure.jwt.service.TokenService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.persistence.auth.UserRepository;
import odyssey.backend.presentation.auth.dto.request.LoginRequest;
import odyssey.backend.domain.auth.exception.UserNotFoundException;
import odyssey.backend.domain.auth.exception.InvalidPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public TokenResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(!checkPassword(request.getPassword(), user.getPassword())){
            throw new InvalidPasswordException();
        }

        return TokenResponse.create(
                tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user)
        );
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
