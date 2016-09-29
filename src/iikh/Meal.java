/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
public class Meal {

    private SimpleStringProperty name;
    private SimpleStringProperty items;
    private SimpleStringProperty date;
    
    private Meal(String name, String items, String date){
    
        this.name = new SimpleStringProperty(name);
        this.items = new SimpleStringProperty(items);
        this.date = new SimpleStringProperty(date);
    }
    
    public static void preparePlan(){
    
        String filename = "meals.txt";
        
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = IIKH.titleGenerator("Prepare a meal plan");
        layout.setTop(header);
        
        Label l1 = new Label("Enter meal name");
        Label l2 = new Label("Enter recipes");
        Label l3 = new Label("Choose date");
        
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        DatePicker dp1 = new DatePicker();
        
        HBox i1 = new HBox();
        HBox i2 = new HBox();
        HBox i3 = new HBox();
        
        i1.setSpacing(10);
        i2.setSpacing(10);
        i3.setSpacing(10);
        
        i1.setPadding(new Insets(10));
        i2.setPadding(new Insets(10));
        i3.setPadding(new Insets(10));
        
        i1.setAlignment(Pos.CENTER_LEFT);
        i2.setAlignment(Pos.CENTER_LEFT);
        i3.setAlignment(Pos.CENTER_LEFT);
        
        i1.getChildren().addAll(l1, tf1);
        i2.getChildren().addAll(l2, tf2);
        i3.getChildren().addAll(l3, dp1);
        
        Button okay = new Button("Save");
        Button cancel = new Button("Cancel");
        okay.setOnAction(e -> {
            String name = tf1.getText();
            String ingredients = tf2.getText();
            String date = (dp1.getValue()).toString();
            String mealData = String.join(":", name, ingredients, date);
            IIKH.writeToFile(mealData, filename);
        });
        cancel.setOnAction(e -> window.close());
        
        HBox confirmation = new HBox();
        confirmation.setAlignment(Pos.CENTER_RIGHT);
        confirmation.getChildren().addAll(okay, cancel);
        
        VBox content = new VBox();
        content.getChildren().addAll(i1, i2, i3, confirmation);
        layout.setCenter(content);
        
        Scene addMeal = new Scene(layout, 400, 250);
        window.setTitle("IIKH");
        window.setScene(addMeal);
        window.show();

    }
    public static void browsePlanDB(boolean isEditable){
    
    ObservableList<Meal> data = FXCollections.observableArrayList();
        
        try{
            FileReader fr = new FileReader("meals.txt");
            BufferedReader bufr = new BufferedReader(fr); 
            String line = bufr.readLine(); 
            while(line != null){
                String[] items = line.split(":");
                Meal newMeal = new Meal(items[0], items[1], items[2]);
                data.add(newMeal);
                line = bufr.readLine(); 
            }
            bufr.close(); 
        }
        catch(IOException e){
            System.out.println("Error while reading file.");
        }
        
        Stage window = new Stage();
        window.setTitle("IIKH");
        window.setWidth(600);
        window.setHeight(400);
        
        TableView table = new TableView();
        table.setEditable(isEditable);
        
        TableColumn nameCol = new TableColumn<>("Name");
        TableColumn itemsCol = new TableColumn("Recipes");
        TableColumn dateCol = new TableColumn("Date");
        
        nameCol.setMinWidth(150);
        itemsCol.setMinWidth(200);
        dateCol.setMinWidth(150);
        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsCol.setCellValueFactory(new PropertyValueFactory<>("items"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
       
        /*
        nameCol.setOnEditCommit((CellEditEvent<Meal, String> t) -> {
                ((Meal) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());

        });

        
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
        table.getColumns().addAll(nameCol, itemsCol, dateCol);
 
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
 
        Scene browse = new Scene(new Group());
        ((Group) browse.getRoot()).getChildren().addAll(vbox);
     
        window.setScene(browse);
        window.show();
    }
    public static void editPlan(){
    
        browsePlanDB(true);
    }
    
    public String getName() {
        return name.get();
    }
    public void setName(String rName) {
        name.set(rName);
    }
        
    public String getItems() {
        return items.get();
    }
    public void setItems(String iName) {
        items.set(iName);
    }
    
    public String getDate() {
        return date.get();
    }
    public void setDate(String mName) {
        date.set(mName);
    }
}
