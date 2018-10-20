package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import java.io.IOException;

public class DBRecordingsViewController extends SideButtons {
    private static String currentDBName = null;

    private static boolean recordingForDB = false;


    @FXML
    private ListView<String> databaseRecordings; // List of user attempt at recording themeselves saying the name

    public void settingDBListView() {
            databaseRecordings.setItems(DataBaseController.getNames());

    }

    @FXML
    public void handlePlayDBRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (databaseRecordings.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please make a selection " + "to play", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        // Check which list has been selected
        else if (!(databaseRecordings.getSelectionModel().isEmpty())) {
            String name = databaseRecordings.getSelectionModel().getSelectedItem();
            String pathToFile = "Database/" + name + "/Database-Recordings/"+ DataBaseController.getNamesHashMap().get(name);
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        }
    }

    @FXML
    public void handleAddNameButton(){

        Main.changeSceneAddDBRecordingsMenu();
    }


}
