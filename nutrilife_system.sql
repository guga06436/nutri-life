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

CREATE TABLE PatientNutritionist(
	patient_id 					INT NOT NULL,
    nutritionist_id 			INT,
    
    PRIMARY KEY(patient_id),
    FOREIGN KEY(patient_id) REFERENCES Patient(patient_id)
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

INSERT INTO Food
VALUES (1, "Arroz", 0, 100, 10, 50, 5, 100, 1, "KILOGRAM");

INSERT INTO Food
VALUES (2, "Macarrão", 0, 100, 10, 50, 5, 100, 1, "KILOGRAM");

INSERT INTO Food
VALUES (3, "Feijão", 0, 100, 10, 50, 5, 100, 1, "KILOGRAM");

INSERT INTO Food
VALUES (4, "Pão", 0, 100, 10, 50, 5, 100, 1, "KILOGRAM");

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

INSERT INTO Vitamin
VALUES (1, "B", 100, "GRAM", 1);

INSERT INTO Vitamin
VALUES (2, "E", 100, "GRAM", 2);

INSERT INTO Vitamin
VALUES (3, "D", 100, "GRAM", 3);

INSERT INTO Vitamin
VALUES (4, "C", 100, "GRAM", 4);

CREATE TABLE MealPlan(
	mealplan_id					INT AUTO_INCREMENT,
    mealplan_name				VARCHAR(60) NOT NULL,
    date_creation				TIMESTAMP NOT NULL,
    goals 						MEDIUMTEXT NOT NULL,
    patient_id 					INT NOT NULL,
    nutritionist_id 			INT NOT NULL,
    
    PRIMARY KEY(mealplan_id),
    FOREIGN KEY(patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY(nutritionist_id) REFERENCES Nutritionist(nutritionist_id)
);

CREATE TABLE Meal(
	meal_id 					INT AUTO_INCREMENT,
    meal_name 					VARCHAR(15) NOT NULL,
    meal_time 					VARCHAR(10) NOT NULL,
    mealplan_id					INT NOT NULL,
    
    PRIMARY KEY(meal_id, mealplan_id),
    FOREIGN KEY(mealplan_id) REFERENCES MealPlan(mealplan_id)
);

CREATE TABLE FoodMeal(
    food_id 					INT NOT NULL,
    portion						DECIMAL NOT NULL,
    portion_unit				VARCHAR(20) NOT NULL,
    meal_id						INT NOT NULL,
    
	CONSTRAINT food_meal_non_negative_portion CHECK (portion > 0.0),
    
    PRIMARY KEY(food_id, meal_id),
    FOREIGN KEY(food_id) REFERENCES Food(food_id),
    FOREIGN KEY(meal_id) REFERENCES Meal(meal_id)
);