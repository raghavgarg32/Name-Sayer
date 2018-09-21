package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class PracticeMenuController implements Initializable {


    private ObservableList<String> items;

    @FXML
    private Button playBtn;

    @FXML
    private Label nameLabel;


    @FXML
    public ListView<String> practiceList; //List of practice names that the user selected

    @FXML
    private ListView<String> userCreations; //List of user attempt at recording themeselves saying the name

    @FXML
    public void handlePlayButton() throws IOException {
        Main.changeSceneRecord();
    }

    @FXML
    public void handleChangeButton() throws IOException {
        Main.changeSceneDataBase();
    }

    @FXML
    public void handleCreateButton() throws IOException {
        ObservableList<String> items =FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App");
        practiceList.setItems(items);
    }


    public void names(ObservableList<String> namesList){
        items =  namesList;

        practiceList.setItems(items);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiceList.getSelectionModel().select(0);

        practiceList.getItems().add("hello");
        practiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + practiceList.getSelectionModel().getSelectedItem());
                nameLabel.setText(practiceList.getSelectionModel().getSelectedItem());
            }
        });
        System.out.println("this is part is workingx");
        System.out.println(DataBaseController.get_practiceSelection());

    }
}
