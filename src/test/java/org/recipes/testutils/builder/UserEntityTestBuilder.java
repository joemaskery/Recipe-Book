package org.recipes.testutils.builder;

import org.recipes.user.entity.UserEntity;
import org.recipes.user.entity.UserEntity.UserEntityBuilder;

public class UserEntityTestBuilder {

    public static UserEntityBuilder userEntity() {
        return UserEntity.builder()
                .firstName("name1")
                .secondName("surname1")
                .email("email1@domain.com");
    }

}
