package org.recipes.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddUserRequest {

    @NotBlank(message = "First Name must not be blank")
    private final String firstName;

    @NotBlank(message = "Second Name must not be blank")
    private final String secondName;

    @NotBlank(message = "Email must not be blank")
    private final String email;

    @NotBlank(message = "Password must not be blank")
    private final String password;

    @NotBlank(message = "Avatar must not be blank")
    private final String avatar;
}
