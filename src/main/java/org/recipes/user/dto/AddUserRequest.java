package org.recipes.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddUserRequest {

    private final String firstName;
    private final String secondName;
    private final String email;
    private final String password1;
    private final String password2;

}
