package org.recipes.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "Email must not be blank")
    private final String email;

    @NotBlank(message = "Password must not be blank")
    private final String password;
}
