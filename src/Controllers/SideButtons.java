package Controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SideButtons {
    @FXML
    public void handleRewardIcon() {
        deleteTempFile();
        Main.changeSceneRewardMenu();
    }

    @FXML
    public void handleHomeIcon() {
        deleteTempFile();
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

    @FXML
    public void handleHelpIcon() {
        deleteTempFile();
        Main.changeSceneHelpMenu();
    }

    @FXML
    public void handleUserRecordingsIcon() {
        deleteTempFile();
        Main.changeSceneUserRecordingsMenu();
    }

    @FXML
    public void handleDBRecordingsIcon() {
        deleteTempFile();
        Main.changeSceneDBRecordingsMenu();
    }

    public void deleteTempFile(){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            Main.changeScenePractice();
        }
    }
}
