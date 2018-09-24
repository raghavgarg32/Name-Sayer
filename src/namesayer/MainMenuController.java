package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwingWorker creationDirectoryWorker = new BashCommandWorker("if [ ! -e \"BadRecordingList.txt\" ]; then\n" +
                "    touch BadRecordingList.txt\n" +
                "fi");
    }
}
