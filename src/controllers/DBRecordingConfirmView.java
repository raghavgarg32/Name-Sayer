package controllers;

import helpers.MakeHeadingNameFit;
import helpers.PlayRecordings;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.ConfirmRecordingsModel;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DBRecordingConfirmView extends SideButtons implements Initializable {
    private ConfirmRecordingsModel confirmRecordingsModel;

    @FXML
    private Label nameLabel;

    /**
     * handleDeleteButton is call back function that is called when the delete button is pressed,
     * it deletes the temp.wav file and goes back to the practice menu
     */
    @FXML
    public void handleDeleteButton() {
        confirmRecordingsModel.deleteRecordings("Database");
        Main.changeScenePractice();

    }

    /**
     * handleSaveButton is a call back function that is called when the save button is pressed, it changes
     * the scene back to the save menu.
     */
    @FXML
    public void handleSaveButton() {
        confirmRecordingsModel.saveDBRecordings();
            Main.changeSceneDBRecordingsMenu();


    }

    /**
     * handlePlayButton is a callback function that is called when the play button is pressed, it plays the current
     * .wav file selected.
     */
    @FXML
    public void handlePlayButton() {

        confirmRecordingsModel.playDBRecording();
    }


    /**
     * handleRedoButton is a callback function that is called when the redo button is pressed, it deletes the temp
     * .wav file and changes scene
     */
    @FXML
    public void handleRedoButton() {
        confirmRecordingsModel.redoUserRecording("Database");
            Main.changeSceneRecord();

    }




    public void setNameLabel(String name){

        nameLabel.setText(MakeHeadingNameFit.changeName(name));

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmRecordingsModel = new ConfirmRecordingsModel();
    }
}
