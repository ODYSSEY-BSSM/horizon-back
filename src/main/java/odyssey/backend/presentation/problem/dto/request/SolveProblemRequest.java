package odyssey.backend.presentation.problem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SolveProblemRequest {

    @NotBlank(message = "필수값입니다.")
    private String answer;

}
