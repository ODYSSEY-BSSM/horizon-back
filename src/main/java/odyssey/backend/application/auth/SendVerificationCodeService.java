package odyssey.backend.application.auth;

import lombok.RequiredArgsConstructor;
import odyssey.backend.domain.auth.SignUpVerification;
import odyssey.backend.infrastructure.mail.MailUtil;
import odyssey.backend.infrastructure.persistence.auth.SignUpVerificationRepository;
import odyssey.backend.presentation.auth.dto.request.SendVerificationRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendVerificationCodeService {

    private final SignUpVerificationRepository signUpVerificationRepository;
    private final MailUtil mailUtil;

    public void sendVerificationCode(SendVerificationRequest request) {
        SignUpVerification signUpVerification = new SignUpVerification(request.getEmail());

        mailUtil.sendMimeMessage(signUpVerification.getEmail(), signUpVerification.getCode());

        signUpVerificationRepository.save(signUpVerification);
    }

}
