package org.recipes.shopping.list.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.SaveShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.shopping.list.service.ShoppingListService;
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
    public ResponseEntity<ShoppingListSummary> buildShoppingList(@RequestBody final BuildShoppingListRequest request) {
        LOG.info("Received request to build shopping list: {}", request);
        return ResponseEntity.ok(shoppingListService.buildShoppingList(request));
    }

    @PostMapping(
            value = {"/save"},
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<SavedShoppingListSummary> saveShoppingList(@RequestBody final SaveShoppingListRequest request) {
        LOG.info("Received request to save a shopping list: [{}]", request.getName());
        return ResponseEntity.ok(shoppingListService.saveShoppingList(request));
    }
}
