package odyssey.backend.user.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.global.jwt.dto.response.TokenResponse;
import odyssey.backend.global.jwt.service.TokenService;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.domain.UserRepository;
import odyssey.backend.user.dto.request.LoginRequest;
import odyssey.backend.user.exception.UserNotFoundException;
import odyssey.backend.user.exception.InvalidPasswordException;
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
