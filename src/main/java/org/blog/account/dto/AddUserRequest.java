package org.blog.account.dto;

import lombok.Data;

@Data
public class AddUserRequest {

    private final String firstName;
    private final String secondName;
    private final String email;
    private final String password1;
    private final String password2;

}
