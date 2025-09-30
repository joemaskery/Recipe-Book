package org.recipes.recipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuantityType {

    ITEMS("items", null),
    GRAM("grams", "g"),
    KILOGRAM("kilograms", "kg"),
    MILLILITRES("millilitres", "mL"),
    LITRES("litres", "L");

    private final String name;
    private final String unit;

}
