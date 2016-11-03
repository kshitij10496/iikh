/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import static iikh.utilities.addConfirmation;
import static iikh.utilities.createHBox;
import static iikh.utilities.parseDate;
import static iikh.utilities.parseString;
import static iikh.utilities.stringMealMerger;
import static iikh.utilities.titleGenerator;
import static iikh.utilities.writeToFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author kshitij10496
 */
public class PlanManager {

    private Date date;
    private Map<String, Meal> mealPlan;
    private Stage window;
    
    public PlanManager(String d, ArrayList<Meal> orderedMeal) {
    
        date = parseDate(d);
        ArrayList<String> timings = new ArrayList<>();
        timings.add("Breakfast");
        timings.add("Lunch");
        timings.add("Snacks");
        timings.add("Dinner");
        mealPlan = stringMealMerger(timings, orderedMeal);
    }
    
    private void addToPlanDB() {
        String dateData = parseString(date);
        String mealsData = "";
        for (Map.Entry<String, Meal> entry : (this.mealPlan).entrySet())
            mealsData += entry.getKey() + ":" + (entry.getValue()).getName() +  ", ";
        
        String planData = String.join(",,", dateData, mealsData);
        writeToFile(planData, "planManager.txt");   
    }
    

    public static void preparePlan(){
    
        Stage window = new Stage();
        BorderPane layout = new BorderPane();
        HBox header = titleGenerator("Prepare new Meal Plan");
        layout.setTop(header);
        
        Label dL = new Label("Choose date");
        DatePicker dP = new DatePicker();
        HBox dateField = createHBox(Pos.CENTER_LEFT);
        HBox.setHgrow(dP, Priority.ALWAYS);
        dateField.getChildren().addAll(dL, dP);
        
        HBox options = createHBox(Pos.CENTER_LEFT);
        Label ti = new Label("Choose timings");
        VBox timings = new VBox(10.0);
        HBox bf = mealOptions("Breakfast");
        HBox lu = mealOptions("Lunch");
        HBox sn = mealOptions("Snacks");
        HBox di = mealOptions("Dinner");
        timings.setAlignment(Pos.CENTER_LEFT);
        timings.setPadding(new Insets(20, 20, 20, 20));
        timings.getChildren().addAll(bf, lu, sn, di);
        options.getChildren().addAll(ti, timings);
        
        HBox confirmation = addConfirmation();
        Node button1 = confirmation.getChildren().get(0);
        if ((button1 instanceof Button) && (((Button)button1).getText() == "Okay")){
        
            Button okay = (Button)button1;
            Button cancel = (Button)confirmation.getChildren().get(1);
            okay.setOnAction(e -> {
                ArrayList<Meal> orderedMeals = new ArrayList<>();
                
                for (int i=0; i < 4; i++){
                    HBox container = (HBox)(timings.getChildren()).get(i);
                    CheckBox timing = (CheckBox) (container.getChildren()).get(0);
                    ChoiceBox choice = (ChoiceBox) (container.getChildren()).get(1);
            
                    Meal newMeal = new Meal();
                
                    if (timing.isSelected()) {
                        if (choice.getValue() == "New Meal")
                            newMeal.createMeal(timing.getText());
                        else
                            newMeal = (Meal)(choice.getValue());
                    }
            
                    else
                        System.out.println("No Meal for " + timing.getText());
        
                    orderedMeals.add(newMeal);
                }
        
                System.out.println("Prepared the Meal Plan !");
                
                PlanManager newPlan = new PlanManager(dP.toString(), orderedMeals);
                newPlan.addToPlanDB();
                        });
            cancel.setOnAction(e -> window.close());
        }
        
        VBox content = new VBox(10);
        content.getChildren().addAll(dateField, options, confirmation);
        layout.setCenter(content);
        
        Scene mealPlan = new Scene(layout, 600, 600);
        window.setTitle("IIKH");
        window.setScene(mealPlan);
        window.show();
    }
    
    private static HBox mealOptions(String checkboxName){
    
        CheckBox cb = new CheckBox(checkboxName);
        ChoiceBox<String> mo = new ChoiceBox<>();
        mo.getItems().add("New Meal");
        ArrayList<String> mealNames = Meal.getAllNames();
        mo.getItems().addAll(mealNames);
        mo.setValue("New Meal");
        
        HBox option = createHBox(Pos.CENTER_LEFT);
        option.getChildren().addAll(cb, mo);
        return option;
    }
    
    private static ArrayList<Meal> checkOptions(VBox timings){
        ArrayList<Meal> mealPlans = new ArrayList<>();
        for (int i=0; i < 4; i++){
            HBox container = (HBox)(timings.getChildren()).get(i);
            CheckBox timing = (CheckBox) (container.getChildren()).get(0);
            ChoiceBox choice = (ChoiceBox) (container.getChildren()).get(1);
            
            Meal newMeal = new Meal();
                
            if (timing.isSelected()) {
                if (choice.getValue() == "New Meal")
                    newMeal.createMeal(timing.getText());
                else
                    newMeal = (Meal)(choice.getValue());
            }
            
            else
                System.out.println("Prepared the Meal for " + timing.getText());
        
            mealPlans.add(newMeal);
        }
        System.out.println("Prepared the Meal Plan !");
        
        return mealPlans;
    }
    public static void editPlan(){}

}