package org.recipes.shopping.list.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.UpdateShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.service.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<SavedShoppingListSummary> buildShoppingList(@RequestBody final BuildShoppingListRequest request) {
        LOG.info("Received request to build shopping list: {}", request);
        return ResponseEntity.ok(shoppingListService.buildAndSaveShoppingList(request));
    }

    @GetMapping(
            value= {"/get/{shoppingListId}"},
            produces = {"application/json"}
    )
    public ResponseEntity<SavedShoppingListSummary> getShoppingList(@PathVariable final String shoppingListId) {
        LOG.info("Received request to get shopping list with ID: {}", shoppingListId);
        return ResponseEntity.ok(shoppingListService.getShoppingListById(shoppingListId));
    }

    @PutMapping(
            value= {"/update"},
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<SavedShoppingListSummary> buildShoppingList(@Valid @RequestBody final UpdateShoppingListRequest request) {
        LOG.info("Received request to update shopping list: {}", request.getId());
        LOG.debug("Received request to update shopping list: {}", request);
        return ResponseEntity.ok(shoppingListService.updateShoppingList(request));
    }
}
