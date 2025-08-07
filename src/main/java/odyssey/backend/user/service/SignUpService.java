package odyssey.backend.user.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.user.domain.UserRepository;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        return SignUpResponse.from(userRepository.save(User.from(request)));
    }

}
