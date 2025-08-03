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
public class UserStats {

    private LocalDateTime dateJoined;
    private Long recipes;
    private Long ingredients;
}
