
package helpers;

import controllers.AddDBRecordingsViewController;
import controllers.Main;
import controllers.RecordViewController;
import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is a helper abstract class that the each controller inherits and this handles the functionality of each side button
 */
public abstract class SideButtons {

    /**
     * A callback function for the reward icon on the tab menu that deletes files and changes scene
     */
    @FXML
    public void handleRewardIcon() {
        RecordViewController.stopRecording();
        deleteTempFile();
        Main.changeSceneRewardMenu();
    }

    /**
     * A callback function for the home icon on the tab menu that deletes files and changes scene
     */
    @FXML
    public void handleHomeIcon() {
        AddDBRecordingsViewController.setIsRecordingForDB(false);
        RecordViewController.stopRecording();
        deleteTempFile();
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

    /**
     * A callback function for the help icon on the tab menu that deletes files and changes scene
     */
    @FXML
    public void handleHelpIcon() {
        RecordViewController.stopRecording();
        deleteTempFile();
        Main.changeSceneHelpMenu();
    }

    /**
     * A callback function for the user recording icon on the tab menu that deletes files and changes scene
     */
    @FXML
    public void handleUserRecordingsIcon() {
        RecordViewController.stopRecording();
        deleteTempFile();
        Main.changeSceneUserRecordingsMenu();
    }

    /**
     * A callback function for the database recording icon on the tab menu that deletes files and changes scene
     */
    @FXML
    public void handleDBRecordingsIcon() {
        RecordViewController.stopRecording();
        deleteTempFile();
        Main.changeSceneDBRecordingsMenu();
    }

    /**
     * Deletes the temporary file that is created as a result of recording
     */
    public void deleteTempFile(){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
        } catch (IOException e) {

        }
    }
}