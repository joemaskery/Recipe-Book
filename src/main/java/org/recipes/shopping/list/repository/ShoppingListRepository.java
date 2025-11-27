package org.recipes.shopping.list.repository;

import org.recipes.shopping.list.entity.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingListRepository extends MongoRepository<ShoppingList, Long> {
}
