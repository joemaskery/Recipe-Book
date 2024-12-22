package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.ShoppingListRequest;
import org.recipes.recipe.dto.response.ShoppingList;
import org.recipes.recipe.service.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping-list")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @PostMapping(
            value= {"/build"},
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<ShoppingList> buildShoppingList(@RequestBody final ShoppingListRequest request) {
        LOG.info("Received request to build shopping list: {}", request);
        return ResponseEntity.ok(shoppingListService.buildShoppingList(request));
    }

}
