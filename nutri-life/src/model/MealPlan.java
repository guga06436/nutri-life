package model;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MealPlan {
    private String planName; 
    private Date creationDate; 
    private String goals; 
    private List<Recipe> recipeList; 
    
    public MealPlan() {
    	
    }

    public MealPlan(String planName, Date creationDate, String goals, List<Recipe> recipeList) {
        this.planName = planName;
        this.creationDate = creationDate;
        this.goals = goals;
        this.recipeList = recipeList;
    }
  
    public void addNewRecipe(Recipe recipe) {
    	recipeList.add(recipe);
    }

}