CREATE TABLE IF NOT EXISTS `ingredients_reference` (
    `ingredients_ref_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100),
    `category` VARCHAR(50),
    `user_id` BIGINT,
    `all_users` boolean,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT FOREIGN KEY (ingredient_ref_id) REFERENCES ingredients_reference(ingredients_ref_id);
