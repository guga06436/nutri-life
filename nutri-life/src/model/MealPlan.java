package model;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MealPlan {
    private String planName; // Nome do plano (até 60 posições alfanuméricas)
    private Date creationDate; // Data de criação
    private String goals; // Objetivos
    private List<Recipe> recipeList; // Lista de receitas

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