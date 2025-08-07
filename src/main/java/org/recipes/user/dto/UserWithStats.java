package org.recipes.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithStats {

    private Integer userId;
    private String firstName;
    private String secondName;
    private String email;
    private UserStats stats;
}
