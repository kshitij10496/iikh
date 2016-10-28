/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import static iikh.utilities.addConfirmation;
import static iikh.utilities.addInput;
import static iikh.utilities.titleGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author kshitij10496
 */
public class Greeter extends Application {
    
    public void initUI(String[] args){
    
        launch(args);
    }

    private static VBox addOptions() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text options[] = new Text[] {
            new Text("1. Add new Recipe."),
            new Text("2. Edit existing Recipe."),
            new Text("3. Browse the Recipe Database."),
            new Text("4. Prepare meal plan."),
            new Text("5. Edit the current meal plan.")};

        for (int i=0; i<5; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 10));
            vbox.getChildren().add(options[i]);
        }
        return vbox;
    }
        
    @Override
    public void start(Stage window) {
        
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("Welcome to IIKH");
        VBox options = addOptions();
        HBox inputField = addInput("Enter your choice", true);
        HBox confirmation = addConfirmation();
        
        Node button1 = confirmation.getChildren().get(0);
        if (button1 instanceof Button){
            ((Button)button1).setOnAction(e -> {
            Node foo = inputField.getChildren().get(1);
            if (foo instanceof TextField) {
                String input = ((TextField)foo).getText();
                int choice = isValidChoice(input);
                if(choice == 0) {
                    System.out.println("Enter a valid choice.");
                }
                else {
                    makeChoice(choice);
                }
            }
            else
                System.out.println("Something is wrong with accessing text input");
            });
        }
        else
            System.out.println("Something is wrong with Button1");
        
        Node button2 = confirmation.getChildren().get(1);
        if (button2 instanceof Button)
            ((Button)button2).setOnAction(e -> window.close());
        else
            System.out.println("Something is wrong with accessing button2");

        VBox content = new VBox();
        content.getChildren().addAll(options, inputField, confirmation);
        
        layout.setTop(header);
        layout.setCenter(content);
        Scene scene = new Scene(layout, 400, 350);
        window.setTitle("IIKH");
        window.setScene(scene);
        window.show();
    }

    private int isValidChoice(String input){ 
        try{
            int choice = Integer.parseInt(input);
            if((choice > 0) && (choice <= 5)) 
                return choice;
            else
                return 0;
        }
        catch(NumberFormatException e){
            return 0;
        }
    }
    
    private void makeChoice(int choice){
        switch(choice){
            
            case 1:
                Recipe.createRecipe();
                break;
                
            case 2:
                Recipe.editRecipe();
                break;
                
            case 3:
                Recipe.browseRecipeDB();
                break;
                
            case 4:
                PlanManager.preparePlan();
                break;
                
            case 5:
                PlanManager.editPlan();
                break;
                
            default:
                System.out.println("No Choice");
        }
    }
}