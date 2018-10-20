package namesayer;

import javafx.fxml.FXML;

public abstract class SideButtons {
	
	/**
	 * An event handler for one of the side buttons on the tab menu of each scene, the method handleRewardIcon changes
	 * the scene to the reward menu
	 */
    @FXML
    public void handleRewardIcon() {
        Main.changeSceneRewardMenu();
    }

	/**
	 * An event handler for one of the side buttons on the tab menu of each scene, the method handleRewardIcon changes
	 * the scene to the reward menu
	 */
    @FXML
    public void handleHomeIcon() {
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

	/**
	 * An event handler for one of the side buttons on the tab menu of each scene, the method handleRewardIcon changes
	 * the scene to the reward menu
	 */
    @FXML
    public void handleHelpIcon() {
        Main.changeSceneHelpMenu();
    }

	/**
	 * An event handler for one of the side buttons on the tab menu of each scene, the method handleRewardIcon changes
	 * the scene to the reward menu
	 */
    @FXML
    public void handleUserRecordingsIcon() {
        Main.changeSceneUserRecordingsMenu();
    }

	/**
	 * An event handler for one of the side buttons on the tab menu of each scene, the method handleDBRecordingsIcon changes
	 * the scene to the database recording menu
	 */
    @FXML
    public void handleDBRecordingsIcon() {
        Main.changeSceneDBRecordingsMenu();
    }
}
