/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import static iikh.utilities.getAllNames;
import static iikh.utilities.writeToFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kshitij10496
 */
public class DatabaseManager {
    
    public static void addRecipeToDB(Recipe newRecipe){
        
        String nameData = newRecipe.getName();
        String ingredientsData = "";
        for (Map.Entry<String, Integer> entry : (newRecipe.ingredients).entrySet())
            ingredientsData += entry.getKey() + ":" + entry.getValue() +  ", ";
        
        String methodData = String.join(". ", newRecipe.method);
        String timeData = Integer.toString(newRecipe.prepTime);
        String servingsData = Integer.toString(newRecipe.servings);

        String recipeData = String.join(",,", nameData, ingredientsData, methodData, timeData, servingsData);
        System.out.println(recipeData);
        writeToFile(recipeData, "recipes.txt");   
    }
        
    public static ArrayList<String> getAllIngredientsName(String name){
        ArrayList<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("recipes.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null){
                if (sCurrentLine.split(",,")[0].equals(name)){
                    System.out.println(sCurrentLine);
                    String ingredientsData = sCurrentLine.split(",,")[1];
                    String[] ingredients = ingredientsData.split(", ");
                    for (String ingredient : ingredients) {
                        items.add((ingredient).split(":")[0]);
                    }
                    
                    break;
                }
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
        System.out.println(items);
        return items;
    }
    
    public static ArrayList<Integer> getAllIngredientsQuantity(String name){
        ArrayList<Integer> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("recipes.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                if (sCurrentLine.split(",,")[0].equals(name)){
                    String ingredientsData = sCurrentLine.split(",,")[1];
                    String[] ingredients = ingredientsData.split(", ");
                    for (String ingredient : ingredients) {
                        items.add(Integer.parseInt((ingredient).split(":")[1]));
                    }
                    break;
                }
	} catch (IOException e) {
            e.printStackTrace();
	}
        return items;
    }
    
    
    public static String getMethodFromDB(String name){
        String method="";
        try (BufferedReader br = new BufferedReader(new FileReader("recipes.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                if (sCurrentLine.split(",,")[0].equals(name)){
                    method += (sCurrentLine.split(",,"))[2];
                    break;
                }
	} catch (IOException e) {
            e.printStackTrace();
	}
        System.out.println(method);
        return method;
    }
    
    
    public static int getTimeFromDB(String name){
        int time=0;
        try (BufferedReader br = new BufferedReader(new FileReader("recipes.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                if (sCurrentLine.split(",,")[0].equals(name)){
                    String prepTime = (sCurrentLine.split(",,"))[3];
                    time = Integer.parseInt(prepTime);
                    break;
                }
	} catch (IOException e) {
            e.printStackTrace();
	}
        System.out.println(time);
        return time;
    }
    
    public static int getServingsFromDB(String name){
        int servings=0;
        try (BufferedReader br = new BufferedReader(new FileReader("recipes.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                if (sCurrentLine.split(",,")[0].equals(name)){
                    String method = (sCurrentLine.split(",,"))[4];
                    servings = Integer.parseInt(method);
                    break;
                }
	} catch (IOException e) {
            e.printStackTrace();
	}
        System.out.println(servings);
        return servings;
    }
    
    public static ObservableList<Recipe> getAllRecipes(){
    
        ObservableList<Recipe> recipes = FXCollections.observableArrayList();
        ArrayList<String> allNames = getAllNames("recipes.txt");
        for(int i = 0; i<allNames.size(); i++){
            Recipe newRecipe = new Recipe(allNames.get(i));
            recipes.add(newRecipe);
        }
        
        return recipes;
    }
}
