CREATE TABLE IF NOT EXISTS `recipes` (
    `recipe_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT,
    `name` VARCHAR(100),
    `description` TEXT,
    `link` TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS `recipe_ingredients` (
    `ingredient_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ingredient_ref_id` BIGINT,
    `recipe_id` BIGINT,
    `quantity` FLOAT,
    `quantity_type` VARCHAR(100),
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);