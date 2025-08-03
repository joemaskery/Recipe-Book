package org.recipes.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsAndStats {

    private Integer userId;
    private String firstName;
    private String secondName;
    private String email;
    private LocalDateTime dateJoined;
    private Long recipes;
    private Long ingredients;
}
