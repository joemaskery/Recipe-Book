package org.blog.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Integer id;
    private String firstName;
    private String secondName;
    private String email;

}
