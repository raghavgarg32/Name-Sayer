package controllers;

import helpers.Alerts;
import helpers.PlayRecordings;
import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.IOException;

/**
 * This is the controller for the DBRecordingsView scene allows the user to see and play all of the database recordings
 */
public class DBRecordingsViewController extends SideButtons {

    @FXML
    private ListView<String> databaseRecordings; // List of user attempt at recording themeselves saying the name

    /**
     * Sets up the database list view with the appropiate names
     */
    public void settingDBListView() {
            databaseRecordings.setItems(HomeViewController.getNamesObservableList());

    }

    /**
     * Allows the user to play the database recording
     * @throws IOException
     */
    @FXML
    public void handlePlayDBRecordingButton() throws IOException {
        // Check which if the list has any item selected
        if (databaseRecordings.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection to play",ButtonType.OK,null);

        }

        else if (!(databaseRecordings.getSelectionModel().isEmpty())) {
            String name = databaseRecordings.getSelectionModel().getSelectedItem();
            String pathToFile = "Database/" + name + "/Database-Recordings/"+ HomeViewController.getNamesHashMap().get(name);
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }

    /**
     * Changes the scene to the add database recording scene to allow user to add name to database
     */
    @FXML
    public void handleAddNameButton(){

        Main.changeSceneAddDBRecordingsMenu();
    }


}
