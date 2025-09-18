package odyssey.backend.domain.roadmap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
    RED("DC2626"),
    ORANGE("EA580C"),
    YELLOW("E6C200"),
    GREEN("16A34A"),
    BLUE("2666DC"),
    PURPLE("A826DC");

    private final String description;

}
