package controllers;

import helpers.Alerts;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Allows the user to add names to enter in name to add to database
 */
public class AddDBRecordingsViewController extends SideButtons {
    private static String currentDBName = null;

    private static boolean isRecordingForDB = false;

    @FXML
    public TextField userName;

    /**
     * The method settingDBListView populates the list view with names currently stored in the database
     */
    @FXML
    public void handleAddRecordingButton() throws IOException {
        //Check if input is valid
        if ((userName.getText().matches("[a-zA-Z]+")) && !(HomeViewController.getNamesWithoutNumbers().contains(userName.getText().toLowerCase()))) {
        isRecordingForDB = true;
        currentDBName = userName.getText().toLowerCase();
        RecordViewController.recordingForNewDBRecording();
        Main.changeSceneRecord();
    } else {

         Alerts.show("Please only enter name with characters A-Z a-z and a name that doesn't exist.",ButtonType.OK,null);

    }
    }

    /**
     * Allows the user to cancel adding new name to the database and go back to the DBRecordingView
     */
    @FXML
    public void handleCancelButton(){
        Main.changeSceneDBRecordingsMenu();
    }

    /**
     * Returns the current name inputed by the user for a database name
     * @return
     */
    public static String getCurrentDBName(){
        return currentDBName;
    }

    /**
     * Returns the boolean value of recordingForDB
     * @return
     */
    public static Boolean getIsRecordingForDB(){
        return isRecordingForDB;
    }

    public static void  setIsRecordingForDB(Boolean state){
        isRecordingForDB = state;
    }

}
