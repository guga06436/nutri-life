package model;

import lombok.Data;
import model.enums.UnitMeasurement;

@Data
public class FoodPortion {
	private float portion;
	private UnitMeasurement unit;
	
	public FoodPortion(float portion, UnitMeasurement unit) {
		this.portion = portion;
		this.unit = unit;
	}
}
