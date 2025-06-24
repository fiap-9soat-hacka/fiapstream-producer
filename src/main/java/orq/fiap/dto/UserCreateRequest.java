package orq.fiap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record UserCreateRequest(@NotEmpty String username, @NotEmpty String password, @Email String email) {
}
