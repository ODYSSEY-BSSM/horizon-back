    package odyssey.backend.application.auth;

    import lombok.RequiredArgsConstructor;
    import odyssey.backend.domain.auth.Role;
    import odyssey.backend.domain.auth.SignUpVerification;
    import odyssey.backend.domain.auth.User;
    import odyssey.backend.infrastructure.persistence.auth.SignUpVerificationRepository;
    import odyssey.backend.infrastructure.persistence.auth.UserRepository;
    import odyssey.backend.presentation.auth.dto.request.SignUpRequest;
    import odyssey.backend.presentation.auth.dto.response.SignUpResponse;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @RequiredArgsConstructor
    @Service
    public class SignUpService {

        private final UserRepository userRepository;
        private final PasswordEncoder bCryptPasswordEncoder;
        private final SignUpVerificationRepository signUpVerificationRepository;

        public SignUpResponse signUp(SignUpRequest request) {
            validate(request.getEmail());

            User user = userRepository.save(User.from(
                    request,
                    bCryptPasswordEncoder.encode(request.getPassword()),
                    Role.USER
            ));

            return SignUpResponse.from(user);
        }

        public void validate(String email) {
            SignUpVerification signUpVerification = signUpVerificationRepository.findById(email)
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일"));

            if(!signUpVerification.isVerified()){
                throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
            }

            signUpVerificationRepository.delete(signUpVerification);
        }

    }
