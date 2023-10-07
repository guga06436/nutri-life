CREATE DATABASE nutrilife;

USE nutrilife;

CREATE TABLE Patient(
	patient_id			INT AUTO_INCREMENT,
    patient_name 		VARCHAR(50) NOT NULL,
    age					INT NOT NULL,
    cpf					VARCHAR(13) NOT NULL,
    height				FLOAT NOT NULL,
    weight 				FLOAT NOT NULL,
    username 			VARCHAR(90) NOT NULL,
    patient_password	VARCHAR(90) NOT NULL,
    
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
    username 					VARCHAR(90) NOT NULL,
    nutritionist_password		VARCHAR(90) NOT NULL,
    
    CONSTRAINT nutritionist_non_negative_age CHECK (age > 0),
    
    PRIMARY KEY(nutritionist_id)
);