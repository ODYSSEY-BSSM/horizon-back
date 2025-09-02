package odyssey.backend.domain.auth;

public enum Role {
    ADMIN,
    USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
