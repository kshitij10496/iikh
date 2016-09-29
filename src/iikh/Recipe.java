package iikh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class Recipe {
    
    private SimpleStringProperty name;
    private SimpleStringProperty ingredients;
    private SimpleStringProperty method;
    private SimpleIntegerProperty time;

    
    public static void addRecipe(){
    
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = IIKH.titleGenerator("Add a recipe");
        layout.setTop(header);
        
        Label l1 = new Label("Enter name");
        Label l2 = new Label("Enter ingredients");
        Label l3 = new Label("Enter method of preparation");
        Label l4 = new Label("Enter approximate time of preparation");
        
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();
        TextField tf4 = new TextField();
        
        HBox i1 = new HBox();
        HBox i2 = new HBox();
        HBox i3 = new HBox();
        HBox i4 = new HBox();
        i1.setSpacing(10);
        i2.setSpacing(10);
        i3.setSpacing(10);
        i4.setSpacing(10);
        i1.setPadding(new Insets(10));
        i2.setPadding(new Insets(10));
        i3.setPadding(new Insets(10));
        i4.setPadding(new Insets(10));
        i1.setAlignment(Pos.CENTER_LEFT);
        i2.setAlignment(Pos.CENTER_LEFT);
        i3.setAlignment(Pos.CENTER_LEFT);
        i4.setAlignment(Pos.CENTER_LEFT);
        
        i1.getChildren().addAll(l1, tf1);
        i2.getChildren().addAll(l2, tf2);
        i3.getChildren().addAll(l3, tf3);
        i4.getChildren().addAll(l4, tf4);
        
        Button okay = new Button("Okay");
        Button cancel = new Button("Cancel");
        okay.setOnAction(e -> {
            String nR = tf1.getText();
            String nI = tf2.getText();
            String nM = tf3.getText();
            int nT = Integer.parseInt(tf4.getText());
            String recipeData = String.join(":", nR, nI, nM, tf4.getText());
            IIKH.writeToFile(recipeData, "recipes.txt");
        });
        cancel.setOnAction(e -> window.close());
        
        HBox confirmation = new HBox();
        confirmation.setAlignment(Pos.CENTER_RIGHT);
        confirmation.getChildren().addAll(okay, cancel);
        
        VBox content = new VBox();
        content.getChildren().addAll(i1, i2, i3, i4, confirmation);
        layout.setCenter(content);
        
        Scene addrecipe = new Scene(layout, 400, 250);
        window.setTitle("Add a new recipe");
        window.setScene(addrecipe);
        window.show();
        
    }
    
    public static void editRecipe(){
    
        browseRecipeDB(true);
    }

    public static void browseRecipeDB(boolean isEditable){
        ObservableList<Recipe> data = FXCollections.observableArrayList();
        
        try{
            FileReader fr = new FileReader("recipes.txt");
            BufferedReader bufr = new BufferedReader(fr); 
            String line = bufr.readLine(); 
            while(line != null){
                String[] items = line.split(":");
                Recipe newRecipe = new Recipe(items[0], items[1], items[2], Integer.parseInt(items[3]));
                data.add(newRecipe);
                line = bufr.readLine(); 
            }
            bufr.close(); 
        }
        catch(IOException e){
            System.out.println("Error while reading file.");
        }
        
        Stage window = new Stage();
        window.setTitle("IIKH");
        window.setWidth(500);
        window.setHeight(400);
        
        TableView table = new TableView();
        table.setEditable(isEditable);
        
        TableColumn<Recipe, String> recipeNameCol = new TableColumn<>("Name");
        TableColumn ingredientsCol = new TableColumn("Ingredients");
        TableColumn methodCol = new TableColumn("Method");
        TableColumn timeCol = new TableColumn("Time");
        
        recipeNameCol.setMinWidth(100);
        ingredientsCol.setMinWidth(150);
        methodCol.setMinWidth(150);
        timeCol.setMinWidth(50);
        
        recipeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        methodCol.setCellValueFactory(new PropertyValueFactory<>("method"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        //recipeNameCol.setCellFactory(TextFieldTableCell.<Recipe>forTableColumn());
        recipeNameCol.setOnEditCommit((CellEditEvent<Recipe, String> t) -> {
                ((Recipe) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());

        });

        /*
        ingredientsCol.setOnEditCommit((CellEditEvent<Recipe, String> t) -> {
                ((Recipe) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());

        });

        methodCol.setOnEditCommit((CellEditEvent<Recipe, String> t) -> {
                ((Recipe) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());

        });
        */
        
        table.setItems(data);
        table.getColumns().addAll(recipeNameCol, ingredientsCol, methodCol, timeCol);
 
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
 
        Scene browse = new Scene(new Group());
        ((Group) browse.getRoot()).getChildren().addAll(vbox);
     
        window.setScene(browse);
        window.show();
    }

    private Recipe(String nR, String nI, String nM, int nT) {
        this.name = new SimpleStringProperty(nR);
        this.ingredients = new SimpleStringProperty(nI);
        this.method = new SimpleStringProperty(nM);
        this.time = new SimpleIntegerProperty(nT);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String rName) {
        name.set(rName);
    }
        
    public String getIngredients() {
        return ingredients.get();
    }
    public void setIngredients(String iName) {
        ingredients.set(iName);
    }
    
    public String getMethod() {
        return method.get();
    }
    public void setEmail(String mName) {
        method.set(mName);
    }
    
    public int getTime() {
        return time.get();
    }
    public void setTime(int tName) {
        time.set(tName);
    }

}