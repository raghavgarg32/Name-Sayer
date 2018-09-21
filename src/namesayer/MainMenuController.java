package namesayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Button practiceBtn;

    @FXML
    private Button quitBtn;

    @FXML
    public void handlePracticeBtn() {
        Main.changeSceneDataBase();
    }

    @FXML
    public void handleQuitBtn() {
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

}
