CREATE TABLE IF NOT EXISTS `recipes` (
    `recipe_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` int,
    `name` VARCHAR(100),
    `description` TEXT,
    `link` TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS `ingredients` (
    `ingredient_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `recipe_id` BIGINT,
    `name` VARCHAR(100),
    `quantity` FLOAT,
    `quantity_type` TEXT,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);