package odyssey.backend.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "필수값입니다.")
    private String email;

    @NotBlank(message = "필수값입니다.")
    private String username;

    @NotBlank(message = "필수값입니다.")
    private String password;

}
