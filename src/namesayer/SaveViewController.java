package namesayer;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class SaveViewController {
    @FXML
    TextField nameTextField;

    private ArrayList<String> nameArrayList = new ArrayList<>();


    public void save(){
        gettingAllRecordings();
        String name = PracticeMenuController.getCurrentName();
        name = gettingRidOfNumbers(name);
        String newName = nameTextField.getText();
        if (nameArrayList.contains(newName + ".wav")){
            Alert alert = new Alert(Alert.AlertType.NONE, "Please enter in a new name ", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else {
            System.out.println("This is the new name " + newName);
            newName = newName.trim();
            if (newName.length() > 0) {
                BashCommandWorker savingFileWorker = new BashCommandWorker("mv ./Database/" + name + "/User-Recordings/temp.wav" +
                        " ./Database/" + name + "/User-Recordings/" + nameTextField.getText() + ".wav");
                try {
                    Thread.sleep(1000);
                }
                    catch (Exception e){

                    }
                Main.changeScenePractice();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Please enter in a valid name ", ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
            }
        }
    }

    public void gettingAllRecordings(){
        String name = PracticeMenuController.getCurrentName();
        name = gettingRidOfNumbers(name);
        File folder = new File(System.getProperty("user.dir")+"/Database/"+name+"/User-Recordings");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nameArrayList.add(listOfFiles[i].getName());
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    public String gettingRidOfNumbers(String nameString){
        if(nameString.contains("-")) {
            nameString = nameString.substring(0, nameString.lastIndexOf("-"));
            System.out.println("This is the current name " +nameString);
        }
        return nameString;
    }

}
