package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MealPlan {
    private String planName; 
    private Date creationDate; 
    private String goals; 
    private List<Meal> meals;
    
    private Patient patient;
    private Nutritionist nutritionist;
    
    public MealPlan() {
		this.meals = new ArrayList<>();
    }

    public MealPlan(String planName, Date creationDate, String goals, List<Meal> meals, Patient patient, Nutritionist nutritionist) {
        this.planName = planName;
        this.creationDate = creationDate;
        this.goals = goals;
        this.meals = meals;
        this.patient = patient;
        this.nutritionist = nutritionist;
    }
    
    public MealPlan(MealPlan mealPlan) {
    	this.planName = mealPlan.getPlanName();
    	this.creationDate = new Date(mealPlan.getCreationDate().getTime());
    	this.goals = mealPlan.goals;
    	this.meals = new ArrayList<>(mealPlan.getMeals());
    	this.patient = new Patient(mealPlan.getPatient());
    	this.nutritionist = new Nutritionist(mealPlan.getNutritionist());
    }

	public void setMeals(List<Meal> meals) {
		this.meals.addAll(meals);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MealPlan other = (MealPlan) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (nutritionist == null) {
			if (other.nutritionist != null)
				return false;
		} else if (!nutritionist.equals(other.nutritionist))
			return false;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		if (planName == null) {
			if (other.planName != null)
				return false;
		} else if (!planName.equals(other.planName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((nutritionist == null) ? 0 : nutritionist.hashCode());
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
		result = prime * result + ((planName == null) ? 0 : planName.hashCode());
		return result;
	}
    
    @Override
    public String toString() {
    	return planName + " - " + "(Patient: " + patient + ")" + "(Responsible Nutritionist: " + nutritionist + ")";
    }
}