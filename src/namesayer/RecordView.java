package namesayer;

import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller for the recording scene
 */
public class RecordView {
    @FXML
    public void handleRecordButton() throws IOException {
        Main.changeSceneRecord();
    }

}
