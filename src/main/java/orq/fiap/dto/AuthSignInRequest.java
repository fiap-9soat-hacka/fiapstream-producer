package orq.fiap.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthSignInRequest(@NotEmpty String username, @NotEmpty String password) {
}
