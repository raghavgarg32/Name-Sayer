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
}
