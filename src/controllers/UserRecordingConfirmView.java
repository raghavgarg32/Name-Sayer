package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import helpers.MakeHeadingNameFit;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.ConfirmRecordingsModel;

public class UserRecordingConfirmView extends SideButtons implements Initializable {
    private ConfirmRecordingsModel confirmRecordingsModel;
    @FXML
    private Label nameLabel;

    @FXML
    private TextField loopNumber;

    private String name;

    private String currentName;

    private String pathToFile;

    private String concatPathToFile;


    /**
     * handleDeleteButton is call back function that is called when the delete
     * button is pressed, it deletes the temp.wav file and goes back to the practice
     * menu
     */
    @FXML
    public void handleDeleteButton() {
        confirmRecordingsModel.deleteRecordings("User-Recordings");
        Main.changeScenePractice();
    }

    /**
     * handleSaveButton is a call back function that is called when the save button
     * is pressed, it changes the scene back to the save menu.
     */
    @FXML
    public void handleSaveButton() {
        confirmRecordingsModel.saveUserRecordings();
        Main.changeScenePractice();
    }


    /**
     * handlePlayButton is a callback function that is called when the play
     * button is pressed, it plays the current .wav file selected.
     */
    @FXML
    public void handlePlayUserButton() {
        confirmRecordingsModel.playUserRecording("User-Recordings");
    }

    /**
     * handleRedoButton is a callback function that is called when the redo button
     * is pressed, it deletes the temp .wav file and changes scene
     */
    @FXML
    public void handleRedoButton() {
        confirmRecordingsModel.redoUserRecording("User-Recordings");
    }

    /**
     * handlePlayDBButton is a callback function that is called when the play button
     * for database recordings is called when the play button for database
     * recordings is called.
     */
    @FXML
    public void handlePlayDBButton() {
        confirmRecordingsModel.playDBRecording();

    }

    public void setNameLabel(String name) {
        confirmRecordingsModel.setCurrentName(name);
        nameLabel.setText(MakeHeadingNameFit.changeName(name));

    }

    public void setLoopNumber(){
        confirmRecordingsModel.setLoopNumber(loopNumber);
    }
    

    @FXML
    public void handleCompareButton() {
        confirmRecordingsModel.compareUserRecordingsWithDB();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmRecordingsModel = new ConfirmRecordingsModel();
        loopNumber.setText("1");

    }

}