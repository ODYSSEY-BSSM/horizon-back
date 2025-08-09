    package odyssey.backend.user.service;

    import lombok.RequiredArgsConstructor;
    import odyssey.backend.user.domain.UserRepository;
    import odyssey.backend.user.domain.User;
    import odyssey.backend.user.dto.request.SignUpRequest;
    import odyssey.backend.user.dto.response.SignUpResponse;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    @RequiredArgsConstructor
    @Service
    public class SignUpService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public SignUpResponse signUp(SignUpRequest request) {
            User user = userRepository.save(User.from(
                    request,
                    bCryptPasswordEncoder.encode(request.getPassword()))
            );

            return SignUpResponse.from(user);
        }

    }
