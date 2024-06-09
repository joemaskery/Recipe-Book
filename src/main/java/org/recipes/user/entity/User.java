package org.recipes.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.entity.Recipe;

import java.util.List;

@Entity(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String firstName;
    private String secondName;
    @Column(unique = true)
    private String email;
    private String password;

    @Transient
    private List<Recipe> recipes;

}
