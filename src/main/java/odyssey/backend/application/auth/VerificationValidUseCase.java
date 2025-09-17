package odyssey.backend.application.auth;

import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.SignUpVerification;
import odyssey.backend.infrastructure.persistence.auth.SignUpVerificationRepository;
import odyssey.backend.presentation.auth.dto.request.VerifyRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationValidUseCase {

    private final SignUpVerificationRepository signUpVerificationRepository;

    public void verify(VerifyRequest request){
        SignUpVerification signUpVerification = signUpVerificationRepository.findById(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않음"));

        if(!signUpVerification.getCode().equals(request.getCode())){
            throw new IllegalArgumentException("인증코드 불일치");
        }

        signUpVerification.verify();

        signUpVerificationRepository.save(signUpVerification);
    }

}
