package controllers;

import helpers.Alerts;
import helpers.PlayRecordings;
import helpers.SideButtons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Shows the user all of the recordings they have created and they can play or delete them
 */
public class UserRecordingsViewController extends SideButtons {
    @FXML
    private ListView<String> userRecordingListView; // List of user attempt at recording themeselves saying the name

    /**
     * Method which will populate a listview with all user recordings for all names in the database
     */
    public void settingUserListView() {
        String tempName = PracticeViewController.getCurrentName(false);

        ObservableList<String> userRecordings = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(tempName != null) {
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].toString().charAt(0) != '.') {

                    String currentFileName = listOfFiles[i].getName().replaceAll("_", " ");
                    
                    userRecordings.add(listOfFiles[i].getName());
                }
            }
            userRecordingListView.setItems(userRecordings);
        }

    }

    /**
     * Callback function that will play the audio of the selected user recording
     * @throws IOException
     */
    @FXML
    public void handlePlayUserRecordingButton() throws IOException {
        // Check which if the user has selected anything from the list
        if (userRecordingListView.getSelectionModel().isEmpty()) {

            Alerts.show("Please make a selection to play",ButtonType.OK,null);

        }
        else if (!(userRecordingListView.getSelectionModel().isEmpty())) {
            String name = userRecordingListView.getSelectionModel().getSelectedItem();
            String pathToFile = "User-Recordings/" + name;
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }

    /**
     * Callback function for the 'delete' button, this method will delete the selected item in the listview, if no
     * item is selected an alert will instead be shown.
     */
    @FXML
    public void handleDeleteButton() {
    	if (userRecordingListView.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection to delete",ButtonType.OK,null);

        }
    	else {
    		try {
				Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+ "/User-Recordings/"+ userRecordingListView.getSelectionModel().getSelectedItem()));
				settingUserListView();
				
			} catch (IOException e) {

			}
    	}
    	RewardMenuController.getRewardPoint();
    }

}
