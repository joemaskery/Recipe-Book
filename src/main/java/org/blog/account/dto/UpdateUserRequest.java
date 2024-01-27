package org.blog.account.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private final Integer userId;
    private final String firstName;
    private final String secondName;
    private final String email;

}
