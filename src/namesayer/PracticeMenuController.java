package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

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

    private SwingWorker<Void, Void> _playWorker;

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
    public ListView<String> practiceList; // List of practice names that the user selected

    @FXML
    private ListView<String> userCreations; // List of user attempt at recording themeselves saying the name

    /**
     * Callback function which is called when the play button is pressed. It checks which list is selected
     * and plays the corresponding item
     * @throws IOException
     */
    @FXML
    public void handlePlayButton() throws IOException {
        // Check which if neither list has been selected
        if (practiceList.getSelectionModel().isEmpty() && userCreations.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please make a selection " + "to play", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        // Check which list has been selected
        else if (practiceList.getSelectionModel().isEmpty() && !(userCreations.getSelectionModel().isEmpty())) {
            String name = userCreations.getSelectionModel().getSelectedItem();

            String pathToFile = "Database/" + getCurrentNameWithoutNumber() + "/User-Recordings/" + name;

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
        
        /**
         * When the user has selected a database recording or a user recording this will allow the user to play the 
         * recording
         */

        else if (userCreations.getSelectionModel().isEmpty() && !(practiceList.getSelectionModel().isEmpty())) {
            String name = practiceList.getSelectionModel().getSelectedItem();
            String nameWithNumber = null;
            int multipleNameIndex = 0;
            List<String> databaseList = DataBaseController.getDatabaseList();
            List<String> nameList = DataBaseController.getNamesWithNumbers();

            // If the name contains a number, remove that number to obtain the name
            if(name.contains("-")) {
                nameWithNumber = name.substring(name.lastIndexOf("-")+1,name.length());
                
                name = name.substring(0, name.lastIndexOf("-"));
          
                multipleNameIndex = Integer.parseInt(nameWithNumber) -1;

            }

  
            String path = databaseList.get(nameList.indexOf(name) + (multipleNameIndex));
  

            String pathToFile = "Database/" + name + "/Database-Recordings/" + path + ".wav";

            // Use a background thread for playing audio
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
	 * @param namesWithoutNumbersList
	 * @param namesWithNumbersList
	 */

    public void names(ObservableList<String> selectedNames, ArrayList<String> namesWithoutNumbersList,
                      ObservableList<String> namesWithNumbersList) {

        items = selectedNames;
        for (String names : namesWithoutNumbersList) {
            namesWithoutNumbers.add(names);
        }

        for (String names : namesWithNumbersList) {
            namesWithNumbers.add(names);
        }

        
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

            
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    items.add(listOfFiles[i].getName());
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

                // Conditional statements to determine which list view has been selected
                if (!(practiceList.getSelectionModel().isEmpty())) {
                    nameLabel.setText(namesWithoutNumbers
                            .get(namesWithNumbers.indexOf(practiceList.getSelectionModel().getSelectedItem())));
                }

                currentName = practiceList.getSelectionModel().getSelectedItem();
                userListView();
                SwingWorker<ArrayList<String>, Integer> gettingRecordingsWorker = new SwingWorker<ArrayList<String>, Integer>() {

                    @Override
                    protected ArrayList<String> doInBackground() throws Exception {
                        if (!(practiceList.getSelectionModel().isEmpty())) {
                            ArrayList<String> nameList = new ArrayList<String>();

                            try {
                                ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "cd Database;\n" + "cd "
                                        + currentName + ";\n" + "cd User-Recordings;\n" + "\n" + "echo $(ls)");
                                Process userRecordingsList = builder.start();

                                InputStream stdout = userRecordingsList.getInputStream();
                                BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                                String line = null;

                                while ((line = stdoutBuffered.readLine()) != null) {
                                    userRecordings.add(line);
                                }
                                stdoutBuffered.close();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                        return null;
                    }

                };
                
                gettingRecordingsWorker.execute();

                for (String recordings : userRecordings) {
                    userRecordingsList.add(recordings);
                }
            }
        });
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