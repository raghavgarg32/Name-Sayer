package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class PracticeMenuController {
    @FXML
    Button playBtn;

    @FXML
    private ListView<String> practiceList; //List of practice names that the user selected

    @FXML
    private ListView<String> userCreations; //List of user attempt at recording themeselves saying the name




    @FXML
    public void handlePlayButton() throws IOException {
        Main.changeSceneRecord();
    }
}
