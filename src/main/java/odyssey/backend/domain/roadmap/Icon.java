package odyssey.backend.domain.roadmap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Icon {
    LANGUAGE("language"),
    CODE("code"),
    SHIELD("shield"),
    DATABASE("database"),
    HOST("host"),
    HTML("html"),
    CSS("css"),
    JAVASCRIPT("javascript"),
    PSYCHIATRY("psychiatry"),
    DEPLOYED_CODE("deployed_code"),
    ASTERISK("asterisk"),
    EXTENSION("extension"),
    SCHOOL("school"),
    FUNCTION("function"),
    PUBLIC("public"),
    NETWORK_INTELLIGENCE("network_intelligence"),
    NETWORK_INTEL_NODE("network_intel_node");

    private final String description;

}