package controllers;

import helpers.Alerts;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddDBRecordingsViewController extends SideButtons {
    private static String currentDBName = null;

    private static boolean isRecordingForDB = false;

    @FXML
    public TextField userName;

    @FXML
    public void handleAddRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if ((userName.getText().matches("[a-zA-Z]+")) && !(HomeViewController.getNamesWithoutNumbers().contains(userName.getText().toLowerCase()))) {
        isRecordingForDB = true;
        currentDBName = userName.getText().toLowerCase();
        RecordView.recordingForNewDBRecording();
        Main.changeSceneRecord();
    } else {

         Alerts.show("Please only enter name with characters A-Z a-z and a name that doesn't exist.",ButtonType.OK,null);

    }
    }



    public static String getCurrentDBName(){
        return currentDBName;
    }

    public static Boolean getIsRecordingForDB(){
        return isRecordingForDB;
    }

}
