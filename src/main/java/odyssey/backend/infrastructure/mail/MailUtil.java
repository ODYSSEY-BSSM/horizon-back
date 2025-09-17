package odyssey.backend.infrastructure.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender javaMailSender;

    public void sendMimeMessage(String email, String code) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(email);

            mimeMessageHelper.setSubject("호라이즌 인증 코드");

            String content = """
                <!DOCTYPE html>
                <html xmlns:th="http://www.thymeleaf.org">
                <body>
                    <div style="margin:100px;">
                        <h1> 인증코드입니다. </h1>
                        <br>
                        <div align="center" style="border:1px solid black; padding: 20px;">
                            <h3> 인증 코드 </h3>
                            <h2 style="color:blue;">%s</h2>
                        </div>
                        <br/>
                    </div>
                </body>
                </html>
                """.formatted(code);

            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
