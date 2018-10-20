package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddDBRecordingsViewController extends SideButtons {
    private static String currentDBName = null;

    private static boolean recordingForDB = false;

    @FXML
    public TextField userName;

    @FXML
    private ListView<String> databaseRecordings; // List of user attempt at recording themeselves saying the name

    public void settingDBListView() {
            databaseRecordings.setItems(DataBaseController.getNames());

    }

    @FXML
    public void handleAddRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if ((userName.getText().matches("[a-zA-Z]+")) && !(DataBaseController.getNamesWithoutNumbers().contains(userName.getText().toLowerCase()))) {
        recordingForDB = true;
        currentDBName = userName.getText().toLowerCase();
        RecordView.recordingForNewDBRecording();
        Main.changeSceneRecord();
    } else {
        Alert alert = new Alert(Alert.AlertType.NONE, "Please only enter name with characters A-Z a-z and a name that doesn't exist.", ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
    }



    public static String getCurrentDBName(){
        return currentDBName;
    }

    public static Boolean getRecordingForDB(){
        return recordingForDB;
    }

}
