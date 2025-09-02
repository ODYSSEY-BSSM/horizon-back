package odyssey.backend.application.auth;

import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.persistence.auth.UserRepository;
import odyssey.backend.presentation.auth.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserInfoService {

    private final UserRepository userRepository;

    public UserResponse getUserInfo(User user) {
        return UserResponse.from(userRepository.findUserByUuid(user.getUuid()));
    }
}

