/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iikh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author kshitij10496
 */
public class utilities {
    
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String parseString(Date date){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
    
    public static Map stringIntMerger(ArrayList<String> list1, ArrayList<Integer> list2){
        Map<String, Integer> newMap = new HashMap<>();
            for (int i=0; i<list1.size(); i++) {
                newMap.put(list1.get(i), list2.get(i));    // is there a clearer way?
        }
        return newMap;
    }
    
    public static Map stringMealMerger(ArrayList<String> list1, ArrayList<Meal> list2){
        Map<String, Meal> newMap = new HashMap<>();
            for (int i=0; i<list1.size(); i++) {
                newMap.put(list1.get(i), list2.get(i));    // is there a clearer way?
        }
        return newMap;
    }
    
    public static HBox titleGenerator(String message){
        Text title = new Text(message);
        HBox header;
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        header = createHBox(Pos.CENTER);
        header.getChildren().add(title);
        return header;
    }
    
    public static HBox createHBox(Pos position ){
        HBox newHBox = new HBox(20.0);
        newHBox.setAlignment(position);
        newHBox.setPadding(new Insets(20, 20, 20, 20));
        return newHBox;
    }
 
    public static HBox addInput(String prompt, boolean isCenter){
        TextField input = new TextField();
        Label inputLabel = new Label(prompt);
        
        HBox inputField;
        if (isCenter)
            inputField = createHBox(Pos.CENTER);
        else
            inputField = createHBox(Pos.CENTER_LEFT);
        
        inputField.getChildren().addAll(inputLabel, input);
        return inputField;
    }

    public static HBox addConfirmation(){
        Button button1 = new Button("Okay");
        Button button2 = new Button("Quit");
        HBox confirmation = createHBox(Pos.CENTER_RIGHT);
        confirmation.getChildren().addAll(button1, button2);
        return confirmation;
    }
    
    public static void writeToFile(String data, String filename){
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
    
    
    public static ArrayList<String> getAllNames(String filename){
        ArrayList<String> mealNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
	{
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
		mealNames.add(sCurrentLine.split(",,")[0]);

	} catch (IOException e) {
            e.printStackTrace();
	}
        return mealNames;
    }


}