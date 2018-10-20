package namesayer;

import javafx.fxml.FXML;

public abstract class SideButtons {
    @FXML
    public void handleRewardIcon() {
        Main.changeSceneRewardMenu();
    }

    @FXML
    public void handleHomeIcon() {
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm ./Concat-Recordings/* ;");
        Main.changeSceneDataBase();
    }

    @FXML
    public void handleHelpIcon() {
        Main.changeSceneHelpMenu();
    }

    @FXML
    public void handleUserRecordingsIcon() {
        Main.changeSceneUserRecordingsMenu();
    }

    @FXML
    public void handleDBRecordingsIcon() {
        Main.changeSceneDBRecordingsMenu();
    }
}
