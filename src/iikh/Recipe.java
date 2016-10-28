/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import static iikh.DatabaseManager.getAllIngredientsName;
import static iikh.DatabaseManager.getAllIngredientsQuantity;
import static iikh.DatabaseManager.getAllRecipes;
import static iikh.DatabaseManager.getMethodFromDB;
import static iikh.DatabaseManager.getServingsFromDB;
import static iikh.DatabaseManager.getTimeFromDB;
import static iikh.utilities.addConfirmation;
import static iikh.utilities.addInput;
import static iikh.utilities.createHBox;
import static iikh.utilities.stringIntMerger;
import static iikh.utilities.titleGenerator;
import java.util.ArrayList;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author kshitij10496
 */
public class Recipe {

    public String name;
    public Map<String, Integer> ingredients;
    public String method;
    public int prepTime;
    public int servings;
    public ArrayList<String> ingredientNames;
    public ArrayList<Integer> ingredientQuantities;
    
    public Recipe(String n, ArrayList<String> ingNames, ArrayList<Integer> ingQuantity, String m,  int pT, int s) {
        name = n;
        ingredients = stringIntMerger(ingNames, ingQuantity);
        ingredientNames = ingNames;
        ingredientQuantities = ingQuantity;
        method = m;
        prepTime = pT;
        servings = s;
    }
    
    public Recipe(String n) {
        this(n, getAllIngredientsName(n), getAllIngredientsQuantity(n), getMethodFromDB(n), getTimeFromDB(n), getServingsFromDB(n));
    }

    public Recipe() {
        
        this(null, new ArrayList<String>(), new ArrayList<Integer>(), null, 0, 0);
    
    }
    
    public static ArrayList<String> getAllNames(){
    
        return utilities.getAllNames("recipes.txt");
    }

    public static VBox addIngsInput(){
    
        VBox ings = new VBox(5); 
        for(int i = 0; i < 5; i++){
        
            HBox newIng = addInput("Ingredient" + (i + 1), false);
            TextField quantity = new TextField();
            quantity.setPromptText("Quantity");
            newIng.getChildren().add(quantity);
            ings.getChildren().add(newIng);
        }
        return ings;
    }
    
    public static void createRecipe(){
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("Add a recipe");
        layout.setTop(header);
        
        HBox i1 = addInput("Enter name", false);
        VBox i2 = addIngsInput();
        HBox i3 = addInput("Enter method of preparation", false);
        Node inputField = i3.getChildren().get(1);
        if (inputField instanceof TextField){
            TextField ipf = (TextField)inputField;
            ipf.setPrefWidth(300);
        }
        
        HBox i4 = addInput("Enter approximate time of preparation", false);
        HBox i5 = addInput("Enter servings", false);
        
        HBox confirmation = addConfirmation();
        Node button1 = confirmation.getChildren().get(0);
        if ((button1 instanceof Button) && (((Button)button1).getText() == "Okay")){
        
            Button okay = (Button)button1;
            Button cancel = (Button)confirmation.getChildren().get(1);
            okay.setOnAction(e -> {
                String nR = ((TextField)(i1.getChildren().get(1))).getText();
                
                ArrayList<String> nIngNames = new ArrayList<>();
                ArrayList<Integer> nIngQuantities = new ArrayList<>();
                for(int i = 0; i < 5; i++){
                    Node hb = i2.getChildren().get(i);
                    System.out.println(i);
                    if (hb instanceof HBox){
                        HBox nhb = (HBox)hb;
                        String nI = ((TextField)(nhb.getChildren().get(1))).getText();
                        String nQ = ((TextField)(nhb.getChildren().get(2))).getText();
                        nIngNames.add(nI);
                        nIngQuantities.add(Integer.parseInt(nQ));
                    }
                }
                
                String nM = ((TextField)(i3.getChildren().get(1))).getText();
                System.out.println(nM);
                
                String nt = ((TextField)(i4.getChildren().get(1))).getText();
                int nT = Integer.parseInt(nt);
                
                String ns = ((TextField)(i5.getChildren().get(1))).getText();
                int nS = Integer.parseInt(ns);
                
                System.out.println(nR + nIngNames+ nIngQuantities+ nM+ nT+ nS);
                
                Recipe newRecipe = new Recipe(nR, nIngNames, nIngQuantities, nM, nT, nS);
                DatabaseManager.addRecipeToDB(newRecipe);
            });
            cancel.setOnAction(e -> window.close());            
        }
        
        else
            System.out.println("Error in accessing confirmation box");
    
        VBox content = new VBox();
        content.getChildren().addAll(i1, i2, i3, i4, i5, confirmation);
        layout.setCenter(content);
        
        Scene addrecipe = new Scene(layout, 600, 750);
        window.setTitle("IIKH");
        window.setScene(addrecipe);
        window.show();
    }
    
    public static void editRecipe(){
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("Edit a recipe");
        layout.setTop(header);
        
        Label inputPrompt = new Label("Enter name of the recipe");
        ChoiceBox<String> ro = new ChoiceBox<>();
        ArrayList<String> recipeNames = Recipe.getAllNames();
        ro.getItems().addAll(recipeNames);
        
        HBox inputField = createHBox(Pos.CENTER);
        inputField.getChildren().addAll(inputPrompt, ro);
        
        HBox confirmation = addConfirmation();
        Node button1 = confirmation.getChildren().get(0);
        if ((button1 instanceof Button) && (((Button)button1).getText() == "Okay")){
        
            Button okay = (Button)button1;
            Button cancel = (Button)confirmation.getChildren().get(1);
            okay.setOnAction(e -> {
                window.setScene(viewRecipe(ro.getValue()));
            });
            cancel.setOnAction(e -> window.close());            
        }
        
        VBox content = new VBox(10);
        content.getChildren().addAll(inputField, confirmation);
        layout.setCenter(content);
        Scene editRecipe = new Scene(layout, 400, 200);
        window.setTitle("Edit a recipe");
        window.setScene(editRecipe);
        window.show();
    }
    
    public static void browseRecipeDB() {
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        HBox header = titleGenerator("Browse recipe database");
        layout.setTop(header);
        
        TableView<Recipe> table = new TableView<>();
        
        TableColumn<Recipe, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Recipe, ArrayList<String>> ingredientNamesColumn = new TableColumn<>("Ingredients");
        ingredientNamesColumn.setMinWidth(200);
        ingredientNamesColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientNames"));
        
        TableColumn<Recipe, ArrayList<String>> methodColumn = new TableColumn<>("Method");
        methodColumn.setMinWidth(200);
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("method"));
        
        TableColumn<Recipe, Integer> prepTimeColumn = new TableColumn<>("Time");
        prepTimeColumn.setMinWidth(50);
        prepTimeColumn.setCellValueFactory(new PropertyValueFactory<>("prepTime"));
        
        TableColumn<Recipe, Integer> servingsColumn = new TableColumn<>("Servings");
        servingsColumn.setMinWidth(50);
        servingsColumn.setCellValueFactory(new PropertyValueFactory<>("servings"));
        
        table.setItems(getAllRecipes());
        table.getColumns().addAll(nameColumn, ingredientNamesColumn, methodColumn, prepTimeColumn, servingsColumn);
        
        VBox content = new VBox(10);
        content.getChildren().add(table);
        layout.setCenter(content);
        Scene editRecipe = new Scene(layout, 700, 400);
        window.setTitle("IIKH");
        window.setScene(editRecipe);
        window.show();
        
    }

    private static Scene viewRecipe(String value) {
        Recipe currentRecipe = DatabaseManager.getRecipeFromDB(value);
        
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("View recipe");
        layout.setTop(header);
        
        HBox i1 = viewInput("Enter name", currentRecipe.getName());
        
        VBox i2 = viewIngsInput(currentRecipe);
        
        HBox i3 = viewInput("Enter method of preparation", currentRecipe.getMethod());
        
        HBox i4 = viewInput("Enter approximate time of preparation", Integer.toString(currentRecipe.getPrepTime()));

        HBox i5 = viewInput("Enter servings", Integer.toString(currentRecipe.getServings()));
        
        HBox confirmation = addConfirmation();
        Node button1 = confirmation.getChildren().get(0);
        if ((button1 instanceof Button) && (((Button)button1).getText() == "Okay")){
        
            Button okay = (Button)button1;
            Button cancel = (Button)confirmation.getChildren().get(1);
            okay.setOnAction(e -> {
                String nR = ((TextField)(i1.getChildren().get(1))).getText();
                
                ArrayList<String> nIngNames = new ArrayList<>();
                ArrayList<Integer> nIngQuantities = new ArrayList<>();
                for(int i = 0; i < 5; i++){
                    Node hb = i2.getChildren().get(i);
                    System.out.println(i);
                    if (hb instanceof HBox){
                        HBox nhb = (HBox)hb;
                        String nI = ((TextField)(nhb.getChildren().get(1))).getText();
                        String nQ = ((TextField)(nhb.getChildren().get(2))).getText();
                        nIngNames.add(nI);
                        nIngQuantities.add(Integer.parseInt(nQ));
                    }
                }
                
                String nM = ((TextField)(i3.getChildren().get(1))).getText();
                System.out.println(nM);
                
                String nt = ((TextField)(i4.getChildren().get(1))).getText();
                int nT = Integer.parseInt(nt);
                
                String ns = ((TextField)(i5.getChildren().get(1))).getText();
                int nS = Integer.parseInt(ns);
                
                System.out.println(nR + nIngNames+ nIngQuantities+ nM+ nT+ nS);
                
                Recipe newRecipe = new Recipe(nR, nIngNames, nIngQuantities, nM, nT, nS);
                DatabaseManager.editRecipeDB(newRecipe);
            });
            //cancel.setOnAction(e -> window.close());            
        }
        
        else
            System.out.println("Error in accessing confirmation box");
    
        VBox content = new VBox();
        content.getChildren().addAll(i1, i2, i3, i4, i5, confirmation);
        layout.setCenter(content);
        
        Scene viewRecipe = new Scene(layout, 600, 800);
        return viewRecipe;
    }


    private static VBox viewIngsInput(Recipe n){
         VBox ings = new VBox(5); 
        for(int i = 0; i < 5; i++){
            HBox newIng = viewInput("Ingredient" + i, n.ingsName(i));
            TextField quantity = new TextField(Integer.toString(n.ingsQuantity(i)));
        
            quantity.setPromptText("Quantity");
            newIng.getChildren().add(quantity);
            ings.getChildren().add(newIng);
        }
        return ings;
        
    }    
    private static HBox viewInput(String prompt, String defaultText){
        
        HBox i = addInput(prompt, false);
        Node inputField = i.getChildren().get(1);
        if (inputField instanceof TextField){
            TextField ipf = (TextField)inputField;
            ipf.setText(defaultText);
        }
        return i;
        }


    public String getName() {
        return this.name;
    }

    public int getPrepTime() {
        return this.prepTime;
    }

    public String getMethod() {
        return this.method;
    }

    public int getServings() {
        return this.servings;
    }

    public String ingsName(int i) {
        return this.ingredientNames.get(i);
    }

    public int ingsQuantity(int i) {
        return this.ingredientQuantities.get(i);
    }
}