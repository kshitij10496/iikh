/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author kshitij10496
 */
public class IIKH extends Application {
    
        
    static void writeToFile(String data, String filename){
        
        try { 
            File targetFile = new File(filename);
            // if file doesnt exists, then create it
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            FileWriter fw = new FileWriter(targetFile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.newLine();
            bw.close();
            System.out.println("Writing to file :" + filename + " is done.");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static HBox titleGenerator(String message){
        Text title = new Text(message);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.getChildren().add(title);
        return header;
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        Stage window = primaryStage;

        BorderPane layout = new BorderPane();
        
        
        HBox header = titleGenerator("Welcome to IIKH");
        layout.setTop(header);
       
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        
        Label options[] = new Label[] {
        new Label("1. Add new Recipe."),
        new Label("2. Edit existing Recipe."),
        new Label("3. Browse the Recipe Database."),
        new Label("4. Prepare meal plan."),
        new Label("5. Edit the current meal plan.")};

        for (int i=0; i<5; i++) {
            vbox.setMargin(options[i], new Insets(0, 0, 0, 10));
            vbox.getChildren().add(options[i]);
        }

        TextField input = new TextField();
        Label inputLabel = new Label("Enter your choice");
        
        HBox inputField = new HBox();
        inputField.setSpacing(10);
        inputField.setAlignment(Pos.CENTER);
        inputField.getChildren().addAll(inputLabel, input);
        
        vbox.getChildren().add(inputField);
        
        Button button1 = new Button();
        Button button2 = new Button();
        
        button1.setText("Okay");
        button2.setText("Quit");
        button1.setOnAction(e -> {
            int choice = isValidChoice(input.getText());
            if(choice == 0) {
                System.out.println("Enter a valid choice.");
            }
            else {
                makeChoice(choice);}
            
        });
        
        button2.setOnAction(e -> window.close());
        
        HBox confirmation = new HBox();
        confirmation.setAlignment(Pos.CENTER_RIGHT);
        confirmation.getChildren().addAll(button1, button2);
        
        vbox.getChildren().add(confirmation);
        layout.setCenter(vbox);
        Scene scene = new Scene(layout, 400, 250);
        
        window.setTitle("IIKH");
        window.setScene(scene);
        window.show();
    }

    public int isValidChoice(String input){
        int choice; 
        try{
            choice = Integer.parseInt(input);
            if((choice > 0) && (choice <= 5)) 
                return choice;
            else
                return 0;
        }
        catch(NumberFormatException e){
            return 0;
    }
}
    
    public void makeChoice(int choice){
        switch(choice){
            
            case 1:
                Recipe.addRecipe();
                break;
                
            case 2:
                Recipe.editRecipe();
                break;
                
            case 3:
                Recipe.browseRecipeDB(false);
                break;
                
            case 4:
                Meal.preparePlan();
                break;
                
            case 5:
                Meal.editPlan();
                break;
                
            default:
                System.out.println("No Choice");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
