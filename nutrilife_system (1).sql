CREATE DATABASE nutrilife;

USE nutrilife;

CREATE TABLE Patient(
	patient_id			INT AUTO_INCREMENT,
    patient_name 		VARCHAR(50) NOT NULL,
    age					INT NOT NULL,
    cpf					VARCHAR(13) NOT NULL,
    height				FLOAT NOT NULL,
    weight 				FLOAT NOT NULL,
    username 			VARCHAR(30) NOT NULL,
    patient_password	VARCHAR(30) NOT NULL,
    
    CONSTRAINT patiente_non_negative_age CHECK (age > 0),
    CONSTRAINT non_negative_height CHECK (height >= 0.0),
    CONSTRAINT non_negative_weight CHECK (weight >= 0.0),
    
    PRIMARY KEY(patient_id)
);

CREATE TABLE Nutritionist(
	nutritionist_id				INT AUTO_INCREMENT,
    nutritionist_name 			VARCHAR(50) NOT NULL,
    age							INT NOT NULL,
    crn							VARCHAR(13) NOT NULL,
    username 					VARCHAR(30) NOT NULL,
    nutritionist_password		VARCHAR(30) NOT NULL,
    
    CONSTRAINT nutritionist_non_negative_age CHECK (age > 0),
    
    PRIMARY KEY(nutritionist_id)
);

CREATE TABLE SystemAdministrator(
	admin_id					INT AUTO_INCREMENT,
    admin_name					VARCHAR(50) NOT NULL,
    username 					VARCHAR(30) NOT NULL,
    admin_password				VARCHAR(50) NOT NULL,
    
    PRIMARY KEY(admin_id)
);

CREATE TABLE Food(
	food_id						INT AUTO_INCREMENT,
    food_name 					VARCHAR(50) NOT NULL,
    food_group					INT NOT NULL,
    calories					DECIMAL NOT NULL,
    proteins 					DECIMAL NOT NULL,
    carbohydrates				DECIMAL NOT NULL,
    lipids						DECIMAL NOT NULL,
    fibers						DECIMAL NOT NULL,
    portion						DECIMAL NOT NULL,
    portion_unit				VARCHAR(20) NOT NULL,
    
    CONSTRAINT non_negative_calories CHECK (calories > 0.0),
    CONSTRAINT non_negative_proteins CHECK (proteins > 0.0),
    CONSTRAINT non_negative_carbohydrates CHECK (carbohydrates > 0.0),
    CONSTRAINT non_negative_lipids CHECK (lipids > 0.0),
    CONSTRAINT non_negative_fibers CHECK (fibers > 0.0),
    CONSTRAINT food_non_negative_portion CHECK (portion > 0.0),
    
    PRIMARY KEY(food_id)
);

CREATE TABLE Vitamin(
	vitamin_id	 				INT AUTO_INCREMENT,
    vitamin_name				VARCHAR(30) NOT NULL,
    portion						DECIMAL NOT NULL,
    portion_unit				VARCHAR(20) NOT NULL,
    food_id 					INT NOT NULL,
    
    CONSTRAINT vitamin_non_negative_portion CHECK (portion > 0.0),
    
    PRIMARY KEY(vitamin_id, food_id),
    FOREIGN KEY(food_id) REFERENCES Food(food_id)
);

CREATE TABLE Recipe(
	recipe_id 					INT AUTO_INCREMENT,
    recipe_name 				VARCHAR(30) NOT NULL,
    sequence_steps 				LONGTEXT NOT NULL,
    
    PRIMARY KEY(recipe_id)
);

CREATE TABLE FoodRecipe(
    food_id 					INT NOT NULL,
    portion						DECIMAL NOT NULL,
    portion_unit				VARCHAR(20) NOT NULL,
    recipe_id					INT NOT NULL,
    
	CONSTRAINT food_recipe_non_negative_portion CHECK (portion > 0.0),
    
    PRIMARY KEY(food_id, recipe_id),
    FOREIGN KEY(food_id) REFERENCES Food(food_id),
    FOREIGN KEY(recipe_id) REFERENCES Recipe(recipe_id)
);

CREATE TABLE MealPlan(
	mealplan_id					INT AUTO_INCREMENT,
    mealplan_name				VARCHAR(60) NOT NULL,
    data_creation				TIMESTAMP NOT NULL,
    goals 						MEDIUMTEXT NOT NULL,
    patient_id 					INT NOT NULL,
    
    PRIMARY KEY(mealplan_id),
    FOREIGN KEY(patient_id) REFERENCES Patient(patient_id)
);

CREATE TABLE RecipeMealPlan(
	recipe_id					INT NOT NULL,
	mealplan_id					INT NOT NULL,
    
    PRIMARY KEY(recipe_id, mealplan_id),
    FOREIGN KEY(recipe_id) REFERENCES Recipe(recipe_id),
    FOREIGN KEY(mealplan_id) REFERENCES MealPlan(mealplan_id)
);