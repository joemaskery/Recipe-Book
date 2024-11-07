package org.recipes.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    private final String email;
    private final String password;

}
