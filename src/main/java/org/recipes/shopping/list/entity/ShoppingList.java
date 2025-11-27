package org.recipes.shopping.list.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.recipes.commons.audit.MongoAuditable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document(value = "shoppingLists")
public class ShoppingList extends MongoAuditable {

    @Id
    private String id;
    private String user;
    private String name;
    private List<ShoppingListItem> items;
}
