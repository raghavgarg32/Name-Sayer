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
import models.ConfirmRecordingsModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class UserRecordingsViewController extends SideButtons implements Initializable {
    private ConfirmRecordingsModel confirmRecordingsModel;
    
    @FXML
    private ListView<String> userRecordingListView; // List of user attempt at recording themeselves saying the name

    public void settingUserListView() {
        String tempName = PracticeMenuController.getCurrentName();

        ObservableList<String> userRecordings = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(tempName != null) {
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String currentFileName = listOfFiles[i].getName().replaceAll("_", " ");
                    System.out.println("This is the current names FILE " + currentFileName);
                    userRecordings.add(listOfFiles[i].getName());
                }
            }
            userRecordingListView.setItems(userRecordings);
        }

    }

    @FXML
    public void handlePlayUserRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (userRecordingListView.getSelectionModel().isEmpty()) {

            Alerts.show("Please make a selection to play",ButtonType.OK,null);

        }
        // Check which list has been selected
        else if (!(userRecordingListView.getSelectionModel().isEmpty())) {
            String name = userRecordingListView.getSelectionModel().getSelectedItem();
            String pathToFile = "User-Recordings/" + name;
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }
    
    @FXML
    public void handleDeleteButton() {
    	if (userRecordingListView.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection to play",ButtonType.OK,null);

        }
    	else {
    		try {
				Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+ "/User-Recordings/"+ userRecordingListView.getSelectionModel().getSelectedItem()));
				settingUserListView();
				System.out.println(Paths.get(System.getProperty("user.dir")+ "/User-Recordings/"+ userRecordingListView.getSelectionModel().getSelectedItem()));
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmRecordingsModel = new ConfirmRecordingsModel();
    }
}
