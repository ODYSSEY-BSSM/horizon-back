package odyssey.backend.presentation.problem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemRequest {

    @NotBlank(message = "필수값입니다.")
    private String title;

    @NotBlank(message = "필수값입니다.")
    private String answer;

}
