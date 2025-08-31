package odyssey.backend.user.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.domain.UserRepository;
import odyssey.backend.user.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserInfoService {

    private final UserRepository userRepository;

    public UserResponse getUserInfo(User user) {
        return UserResponse.from(userRepository.findUserByUuid(user.getUuid()));
    }
}

