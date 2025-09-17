package odyssey.backend.presentation.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendVerificationRequest {

    @NotEmpty(message = "필수값입니다.")
    private String email;

}
