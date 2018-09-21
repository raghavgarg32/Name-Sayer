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

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PracticeMenuController implements Initializable {


    private ObservableList<String> items;

    private ArrayList<String> namesWithoutNumbers;

    private ArrayList<String> namesWithNumbers;

    private ArrayList<String> userRecordings;

    private static String currentName;

    private ObservableList<String> userRecordingsList;

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


    public void names(ObservableList<String> selectedNames, ArrayList<String> namesWithoutNumbersList,
                      ObservableList<String> namesWithNumbersList){
        items =  selectedNames;
        for(String names : namesWithoutNumbersList){
            namesWithoutNumbers.add(names);
        }

        for(String names : namesWithNumbersList){
            namesWithNumbers.add(names);
        }

        System.out.println("this is playing " + namesWithNumbersList);
        practiceList.setItems(items);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiceList.getSelectionModel().select(0);
        namesWithoutNumbers = new ArrayList<String>();
        namesWithNumbers = new ArrayList<String>();
        userRecordings = new ArrayList<String>();
        userRecordingsList =FXCollections.observableArrayList ();
        currentName = "Name";
        practiceList.getItems().add("hello");
        practiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                nameLabel.setText(namesWithoutNumbers.get(namesWithNumbers.indexOf(practiceList.getSelectionModel().getSelectedItem())));
                currentName = practiceList.getSelectionModel().getSelectedItem();
                System.out.println(currentName);
                SwingWorker gettingRecordingsWorker = new SwingWorker<ArrayList<String>, Integer>() {

                    @Override
                    protected ArrayList<String> doInBackground() throws Exception {
                        ArrayList<String> nameList = new ArrayList<String>();

                        try {
                            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "cd Database;\n" +
                                    "cd "+currentName+";\n" +
                                    "cd User-Recordings;\n" +
                                    "\n" +
                                    "echo $(ls)");
                            Process process = builder.start();

                            InputStream stdout = process.getInputStream();
                            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                            String line = null;

                            while ((line = stdoutBuffered.readLine()) != null) {
                                userRecordings.add(line);
                            }
                            stdoutBuffered.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        return null;
                    }

                };
                gettingRecordingsWorker.execute();

                for (String recordings : userRecordings){
                    userRecordingsList.add(recordings);
                }
                userCreations.setItems(userRecordingsList);
            }
        });
    }

    public static String getCurrentName(){
        System.out.println("Current name" + currentName);
        return currentName;
    }
}