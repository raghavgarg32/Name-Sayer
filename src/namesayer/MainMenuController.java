package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button practiceBtn;

    @FXML
    private Button quitBtn;

    /**
     * Changes the scene to database when called
     */
    @FXML
    public void handlePracticeBtn() {
        Main.changeSceneDataBase();
    }

    /**
     * Exits the application when called
     */
    @FXML
    public void handleQuitBtn() {
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BashCommandWorker creationDirectoryWorker = new BashCommandWorker("if [ ! -e \"BadRecordingList.txt\" ]; then\n" +
                "    touch BadRecordingList.txt\n" +
                "fi");

    }

}