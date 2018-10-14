package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;

public class UserRecordingsViewController extends SideButtons {
    @FXML
    private ListView<String> userCreations; // List of user attempt at recording themeselves saying the name

    public void settingUserListView() {
        String tempName = PracticeMenuController.getCurrentName();


        ObservableList<String> items = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(tempName != null) {
            File[] listOfFiles = folder.listFiles();


            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String currentFileName = listOfFiles[i].getName().replaceAll("_", " ");
                    System.out.println("This is the current names FILE " + currentFileName);
                    items.add(listOfFiles[i].getName());
                }
            }

            userCreations.setItems(items);
        }

    }

    @FXML
    public void handlePlayUserRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (userCreations.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please make a selection " + "to play", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        // Check which list has been selected
        else if (!(userCreations.getSelectionModel().isEmpty())) {
            String name = userCreations.getSelectionModel().getSelectedItem();
            String pathToFile = "User-Recordings/" + name;
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }



}
