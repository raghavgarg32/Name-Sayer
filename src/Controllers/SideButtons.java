package Controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SideButtons {
	
	/**
	 * A callback function for the reward icon on the tab menu that deletes files and changes scene
	 */
    @FXML
    public void handleRewardIcon() {
        deleteTempFile();
        Main.changeSceneRewardMenu();
    }

	/**
	 * A callback function for the home icon on the tab menu that deletes files and changes scene
	 */
    @FXML
    public void handleHomeIcon() {
        deleteTempFile();
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

	/**
	 * A callback function for the help icon on the tab menu that deletes files and changes scene
	 */
    @FXML
    public void handleHelpIcon() {
        deleteTempFile();
        Main.changeSceneHelpMenu();
    }

	/**
	 * A callback function for the user recording icon on the tab menu that deletes files and changes scene
	 */
    @FXML
    public void handleUserRecordingsIcon() {
        deleteTempFile();
        Main.changeSceneUserRecordingsMenu();
    }

	/**
	 * A callback function for the database recording icon on the tab menu that deletes files and changes scene
	 */
    @FXML
    public void handleDBRecordingsIcon() {
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
            e.printStackTrace();
        }
    }
}
