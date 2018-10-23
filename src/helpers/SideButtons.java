
package helpers;

import controllers.Main;
import controllers.RecordView;
import helpers.BashCommandWorker;
import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SideButtons {

    @FXML
    public void handleRewardIcon() {
        RecordView.stopRecording();
        deleteTempFile();
        Main.changeSceneRewardMenu();
    }

    @FXML
    public void handleHomeIcon() {
        RecordView.stopRecording();
        deleteTempFile();
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

    @FXML
    public void handleHelpIcon() {
        RecordView.stopRecording();
        deleteTempFile();
        Main.changeSceneHelpMenu();
    }

    @FXML
    public void handleUserRecordingsIcon() {
        RecordView.stopRecording();
        deleteTempFile();
        Main.changeSceneUserRecordingsMenu();
    }

    @FXML
    public void handleDBRecordingsIcon() {
        RecordView.stopRecording();
        deleteTempFile();
        Main.changeSceneDBRecordingsMenu();
    }

    public void deleteTempFile(){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}