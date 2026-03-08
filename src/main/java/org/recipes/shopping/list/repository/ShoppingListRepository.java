package org.recipes.shopping.list.repository;

import org.recipes.shopping.list.entity.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends MongoRepository<ShoppingList, String> {
}
