package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PracticeMenuController implements Initializable {

    private ObservableList<String> items;

    private static SwingWorker<Void, Void> _playWorker;

    private ArrayList<String> namesWithoutNumbers;

    private ArrayList<String> namesWithNumbers;

    private ArrayList<String> userRecordings;

    private static String currentName;

    private ObservableList<String> userRecordingsList;

@FXML
public Label names;

    @FXML
    public ListView<String> practiceList; // List of practice names that the user selected

    @FXML
    private ListView<String> userCreations; // List of user attempt at recording themeselves saying the name



    /**
     * Callback function which is called when the play button is pressed. It checks which list is selected
     * and plays the corresponding item
     * @throws IOException
     */
    @FXML
    public void handlePlayDBRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (practiceList.getSelectionModel().isEmpty()) {
            emptyListViewPopup();
        }
        // Check which list has been selected
        else if (!(practiceList.getSelectionModel().isEmpty())) {
            if(currentName.contains(" ") || currentName.contains("-")) {
                String name = practiceList.getSelectionModel().getSelectedItem().replaceAll(" ", "_");

                String pathToFile = "Concat-Recordings/" + name + ".wav";

                handlingPlayingRecordings(pathToFile);
            } else {
                String name = currentName;

                List<String> databaseList = DataBaseController.getDatabaseList();
                List<String> nameList = DataBaseController.getNames();

                String path = databaseList.get(nameList.indexOf(name));
                String pathToFile = "Database/"+name+"/Database-Recordings/"+path+".wav";
                PracticeMenuController.handlingPlayingRecordings(pathToFile);
            }
        }
    }

    public void emptyListViewPopup(){
        Alert alert = new Alert(Alert.AlertType.NONE, "Please make a selection " + "to play", ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void handlingPlayingRecordings(String pathToFile){

        _playWorker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                AudioInputStream stream;
                AudioFormat format;
                DataLine.Info info;
                SourceDataLine sourceLine;

                try {
                    stream = AudioSystem.getAudioInputStream(new File(pathToFile));
                    format = stream.getFormat();

                    info = new DataLine.Info(SourceDataLine.class, format);
                    sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceLine.open(format);

                    sourceLine.start();

                    int nBytesRead = 0;
                    int BUFFER_SIZE = 128000;
                    byte[] abData = new byte[BUFFER_SIZE];
                    while (nBytesRead != -1) {
                        try {
                            nBytesRead = stream.read(abData, 0, abData.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (nBytesRead >= 0) {
                            @SuppressWarnings("unused")
                            int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                        }
                    }

                    sourceLine.drain();
                    sourceLine.close();

                } catch (Exception e) {

                }
                return null;
            }

        };
        _playWorker.execute();
    }


    @FXML
    public void handlePlayUserRecordingButton() throws IOException {
        // Check which if neither list has been selected
        if (userCreations.getSelectionModel().isEmpty()) {
            emptyListViewPopup();
        }
        // Check which list has been selected
        else if (!(userCreations.getSelectionModel().isEmpty())) {
            String name = userCreations.getSelectionModel().getSelectedItem();
            String pathToFile = "User-Recordings/" + name;
            handlingPlayingRecordings(pathToFile);
        }

        /**
         * When the user has selected a database recording or a user recording this will allow the user to play the
         * recording
         */

        else if (userCreations.getSelectionModel().isEmpty() && !(practiceList.getSelectionModel().isEmpty())) {
            String name = practiceList.getSelectionModel().getSelectedItem().replaceAll(" ", "_");

            String pathToFile = "Concat-Recordings/" + name + ".wav";


            // Use a background thread for playing audio
            _playWorker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    AudioInputStream stream;
                    AudioFormat format;
                    DataLine.Info info;
                    SourceDataLine sourceLine;

                    try {
                        System.out.println("Path to file " + pathToFile);
                        stream = AudioSystem.getAudioInputStream(new File(pathToFile));
                        format = stream.getFormat();

                        info = new DataLine.Info(SourceDataLine.class, format);
                        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                        sourceLine.open(format);

                        sourceLine.start();

                        int nBytesRead = 0;
                        int BUFFER_SIZE = 128000;
                        byte[] abData = new byte[BUFFER_SIZE];
                        while (nBytesRead != -1) {
                            try {
                                nBytesRead = stream.read(abData, 0, abData.length);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (nBytesRead >= 0) {
                                @SuppressWarnings("unused")
                                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                            }
                        }

                        sourceLine.drain();
                        sourceLine.close();

                    } catch (Exception e) {

                    }
                    return null;
                }
            };
            _playWorker.execute();
        }
    }


    /**
     * Allows the user to change their selected names to practice by changing the scene to Database scene
     * @throws IOException
     */
    @FXML
    public void handleChangeButton() throws IOException {
        Main.changeSceneDataBase();
    }

    /**
     * Allows the user create recordings for a selected name and makes sure that the user has selected a recording
     * to practice by changing the scene to Record scene
     * @throws IOException
     */
    @FXML
    public void handleCreateButton() throws IOException {
        if (practiceList.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please make a selection first", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            Main.changeSceneRecord();
        }
    }

    /**
     * Allows the user rate user recordings for a selected name and makes sure that the user has selected a recording
     * to practice by changing the scene to Rate scene
     * @throws IOException
     */
    @FXML
    public void handleRateButton() throws IOException {
        if (practiceList.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please select a database recording " + "to review", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            Main.changeSceneRateMenu();
        }

    }

    /**
	 * Setting up different array lists for the different list views to use
	 * @param selectedNames

	 */

    public void names(ObservableList<String> selectedNames) {

        items = selectedNames;

        practiceList.setItems(items);
    }

	/**
	 * Places user recordings into the list view
	 */
    public void userListView() {
        String tempName = PracticeMenuController.getCurrentName();
        if (!(practiceList.getSelectionModel().isEmpty()) && PracticeMenuController.getCurrentName().contains("-")) {
            tempName = PracticeMenuController.getCurrentName().substring(0,
                    PracticeMenuController.getCurrentName().lastIndexOf("-"));
        }


        ObservableList<String> items = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.dir") + "/Database/" + tempName + "/User-Recordings");
        if(tempName != null) {
            File[] listOfFiles = folder.listFiles();


//            for (int i = 0; i < listOfFiles.length; i++) {
//                if (listOfFiles[i].isFile()) {
//                    items.add(listOfFiles[i].getName());
//                }
//            }

//            userCreations.setItems(items);
        }

    }

    public void settingUserListView(String currentName) {
        String tempName = PracticeMenuController.getCurrentName();


        ObservableList<String> items = FXCollections.observableArrayList();
        File folder = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(tempName != null) {
            File[] listOfFiles = folder.listFiles();


            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String currentFileName = listOfFiles[i].getName().replaceAll("_", " ");
                    if (currentFileName.contains(currentName)){
                        items.add(listOfFiles[i].getName());
                    }
                }
            }

            userCreations.setItems(items);
        }

    }


    /**
	 * Allows user to click on different names and change name label and user recording array list accordingly
	 * @param location
	 * @param resources
	 */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiceList.getSelectionModel().select(0);
        namesWithoutNumbers = new ArrayList<String>();
        namesWithNumbers = new ArrayList<String>();
        userRecordings = new ArrayList<String>();
        userRecordingsList = FXCollections.observableArrayList();
        currentName = "Name";
        practiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	//Changes the practice view depending on which was the last selected list
            @Override
            public void handle(MouseEvent event) {
                userCreations.getSelectionModel().clearSelection();
                System.out.println(practiceList.getSelectionModel().getSelectedItem());
                currentName = practiceList.getSelectionModel().getSelectedItem();
                String tempCurrentName = currentName;
                settingUserListView(currentName);
                if (tempCurrentName.length() > 20){
                    tempCurrentName = tempCurrentName.substring(0,17);
                    tempCurrentName = tempCurrentName + "...";
                }
                names.setText(tempCurrentName);
            }
        });
    }



    public static String getSelectedName(){
        return currentName;
    }
    /**
     * Onclick callback function for the userCreations list
     */
    @FXML
    public void userCreationsListClicked() {

        practiceList.getSelectionModel().clearSelection();
    }

    /**
     * Callback function for the shuffle button which randomizes the list view
     */
    @FXML
    public void handleShuffleButton() {
        ObservableList<String> tempList = FXCollections.observableArrayList();
        List<String> praticeSelection = DataBaseController._practiceSelection;
        Collections.shuffle(praticeSelection);
        tempList.addAll(praticeSelection);
        practiceList.setItems(tempList);

    }

    /**
     * Getter method to get the current name
     * @return
     */
    public static String getCurrentName() {
        System.out.println(currentName);
        return currentName;
    }

    /**
     * getter method to get the current name but with the number removed
     * @return
     */
    public static String getCurrentNameWithoutNumber() {
        if (currentName.contains("-")) {
            currentName = currentName.substring(0, currentName.lastIndexOf("-"));
        }
        return currentName;
    }

}