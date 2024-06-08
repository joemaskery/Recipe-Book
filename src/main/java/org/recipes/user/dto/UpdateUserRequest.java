package org.recipes.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {

    private final Integer userId;
    private final String firstName;
    private final String secondName;
    private final String email;

}
