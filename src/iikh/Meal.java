package iikh;

import static iikh.utilities.addConfirmation;
import static iikh.utilities.addInput;
import static iikh.utilities.createHBox;
import static iikh.utilities.titleGenerator;
import static iikh.utilities.writeToFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kshitij10496
 */
public class Meal {

   
    private String name;
    private ArrayList<Recipe> items;
    
    public Meal(String n, ArrayList<Recipe> i) {
        name = n;
        items = i;
    }

    public Meal() {
        name = "";
        items = null;
    }
    
    public Meal(String n){
        if (getAllNames().contains(n)) {
            name = n;
            items = getAllItems(n);
        }
        else {
            name = "";
            items = null;
        }
    }
    
    void addToMealDB() {
        String nameData = name;
        String itemsData = "";
        for (Recipe r : items)
            itemsData += r.getName() + ", ";
        
        String planData = String.join(",,", nameData, itemsData);
        writeToFile(planData, "meals.txt");  
    }

    String getName() {
        return this.name;
    }

    private ArrayList<Recipe> getAllItems(String name){
        ArrayList<Recipe> allItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("meals.txt")))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                if (sCurrentLine.split(",,")[0] == name){
                    List<String> foo = Arrays.asList((sCurrentLine.split(",,")[1]).split(", "));
                    Recipe newRecipe;
                    for(int i = 0; i < foo.size(); i++) {
                        String n = foo.get(i);
                        newRecipe = Recipe(n);
                        allItems.add(newRecipe);
                    }
                    break;
                }
	} catch (IOException e) {
            e.printStackTrace();
	}
        return allItems;
    }
    
    public static ArrayList<String> getAllNames(){
        return utilities.getAllNames("meals.txt");
    }


    private Recipe Recipe(String n) {
        return Recipe(n);
    }

    void createMeal(String text) {
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("Add a meal for " + text);
        layout.setTop(header);
        
        HBox i1 = addInput("Enter name", false);
        
        VBox allRecipeItems = new VBox(10);
        HBox o1 = recipeOptions("Recipe1");
        HBox o2 = recipeOptions("Recipe2");
        HBox o3 = recipeOptions("Recipe3");
        HBox o4 = recipeOptions("Recipe4");
        HBox o5 = recipeOptions("Recipe5");
        allRecipeItems.getChildren().addAll(o1, o2, o3, o4, o5);
        
        HBox confirmation = addConfirmation();
        Node button1 = confirmation.getChildren().get(0);
        if ((button1 instanceof Button) && (((Button)button1).getText() == "Okay")){
        
            Button okay = (Button)button1;
            Button cancel = (Button)confirmation.getChildren().get(1);
            okay.setOnAction(e -> {
                String nM = ((TextField)(i1.getChildren().get(1))).getText();
                ArrayList<Recipe> recipes = checkOptions(allRecipeItems);
                
                Meal newMeal = new Meal(nM, recipes);
                newMeal.addToMealDB();
            });
            cancel.setOnAction(e -> window.close());            
        }
        
        else
            System.out.println("Error in accessing confirmation box");
    
        
        VBox content = new VBox();
        content.getChildren().addAll(i1, allRecipeItems, confirmation);
        layout.setCenter(content);
        
        Scene addrecipe = new Scene(layout, 600, 600);
        window.setTitle("Add a new recipe");
        window.setScene(addrecipe);
        window.show();
}
    
    
    private static HBox recipeOptions(String checkboxName){
    
        CheckBox cb = new CheckBox(checkboxName);
        ChoiceBox<String> ro = new ChoiceBox<>();
        ro.getItems().add("New Recipe");
        ArrayList<String> recipeNames = Recipe.getAllNames();
        ro.getItems().addAll(recipeNames);
        ro.setValue("New Recipe");
        
        HBox option = createHBox(Pos.CENTER_LEFT);
        option.getChildren().addAll(cb, ro);
        return option;
    }

    private static ArrayList<Recipe> checkOptions(VBox timings){
        ArrayList<Recipe> items = new ArrayList<>();
        System.out.println("Entered checkOptions");
        for (int i=0; i < 4; i++){
            HBox container = (HBox)(timings.getChildren()).get(i);
            CheckBox timing = (CheckBox) (container.getChildren()).get(0);
            ChoiceBox choice = (ChoiceBox) (container.getChildren()).get(1);
            System.out.println(i);
            
            Recipe newRecipe = new Recipe();
                
            if (timing.isSelected()) {
                System.out.println("Choosing Recipe");
        
               if (choice.getValue() == "New Recipe"){
                    System.out.println("Creating new Recipe");
        
                    Recipe.createRecipe();}
               else{
                    System.out.println("Adding old recipe");
        
                    newRecipe = (Recipe)(choice.getValue());
               }
        
                DatabaseManager.addRecipeToDB(newRecipe);
                System.out.println("Saced to DB");
            }
            
            else
                System.out.println("Prepared the Meal for " + timing.getText());
        
            items.add(newRecipe);
        }
        System.out.println("Prepared the Meal Plan !");
        
        return items;
    }


}
