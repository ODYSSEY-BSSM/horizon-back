    package odyssey.backend.application.auth;

    import lombok.RequiredArgsConstructor;
    import odyssey.backend.domain.auth.Role;
    import odyssey.backend.infrastructure.persistence.auth.UserRepository;
    import odyssey.backend.domain.auth.User;
    import odyssey.backend.presentation.auth.dto.request.SignUpRequest;
    import odyssey.backend.presentation.auth.dto.response.SignUpResponse;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @RequiredArgsConstructor
    @Service
    public class SignUpService {

        private final UserRepository userRepository;
        private final PasswordEncoder bCryptPasswordEncoder;

        public SignUpResponse signUp(SignUpRequest request) {
            User user = userRepository.save(User.from(
                    request,
                    bCryptPasswordEncoder.encode(request.getPassword()),
                    Role.USER
            ));

            return SignUpResponse.from(user);
        }

    }
