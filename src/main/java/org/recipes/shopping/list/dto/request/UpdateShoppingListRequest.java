package org.recipes.shopping.list.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.commons.model.QuantityType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShoppingListRequest {

    @NotBlank(message = "Shopping list ID must not be blank")
    private String id;

    @NotBlank(message = "Shopping list name must not be blank")
    private String name;

    @NotEmpty(message = "Shopping list must have items")
    @Valid
    private List<ShoppingListItem> items;

    public record ShoppingListItem(
            @NotBlank(message = "Item name must not be blank")
            String name,

            @NotNull(message = "Item quantity must not be null")
            Double quantity,

            @NotNull(message = "Item quantity type must not be null")
            QuantityType quantityType,

            @NotBlank(message = "Item category must not be blank")
            String category
    ) {}
}
