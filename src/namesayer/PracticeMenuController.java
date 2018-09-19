package namesayer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PracticeMenuController implements Initializable {

    private ObservableList<String> items;

    @FXML
    private Button changeBtn;
    
    @FXML
    private Button playBtn;

    @FXML
    private ListView<String> practiceList; //List of practice names that the user selected

    @FXML
    private ListView<String> userCreations; //List of user attempt at recording themeselves saying the name

    @FXML
    public void handlePlayButton() throws IOException {
        Main.changeSceneRecord();
    }

    @FXML
    public void handleCreateButton() throws IOException {
        Main.changeSceneRecord();
    }

    @FXML
    public void handleChangeButton() throws IOException {
    	Main.changeSceneDataBase();
    } 
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
