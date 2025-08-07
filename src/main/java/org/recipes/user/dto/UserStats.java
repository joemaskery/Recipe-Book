package org.recipes.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {

    private LocalDate dateJoined;
    private Long recipes;
    private Long ingredients;
}
