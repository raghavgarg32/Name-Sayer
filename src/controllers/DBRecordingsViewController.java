package controllers;

import helpers.Alerts;
import helpers.PlayRecordings;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.IOException;

public class DBRecordingsViewController extends SideButtons {
    private static String currentDBName = null;

    private static boolean recordingForDB = false;


    @FXML
    private ListView<String> databaseRecordings; // List of user attempt at recording themeselves saying the name

    public void settingDBListView() {
            databaseRecordings.setItems(HomeViewController.getAllNamesOfDatabaseRecording());

    }

    @FXML
    public void handlePlayDBRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (databaseRecordings.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection to play",ButtonType.OK,null);

        }
        // Check which list has been selected
        else if (!(databaseRecordings.getSelectionModel().isEmpty())) {
            String name = databaseRecordings.getSelectionModel().getSelectedItem();
            String pathToFile = "Database/" + name + "/Database-Recordings/"+ HomeViewController.getNamesHashMap().get(name);
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }

    @FXML
    public void handleAddNameButton(){

        Main.changeSceneAddDBRecordingsMenu();
    }


}
