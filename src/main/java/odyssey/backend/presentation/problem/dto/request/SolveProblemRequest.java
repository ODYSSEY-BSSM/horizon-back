package odyssey.backend.presentation.problem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SolveProblemRequest {

    @NotBlank(message = "필수값입니다.")
    private String answer;

}
