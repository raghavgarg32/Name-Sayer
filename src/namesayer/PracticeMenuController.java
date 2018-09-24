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

        else if (userCreations.getSelectionModel().isEmpty() && !(practiceList.getSelectionModel().isEmpty())) {
            String name = practiceList.getSelectionModel().getSelectedItem();
            String nameWithNumber = null;
            int multipleNameIndex = 0;
            List<String> databaseList = DataBaseController.getDatabaseList();
            List<String> nameList = DataBaseController.getNamesWithNumbers();

            // If the name contains a number, remove that number to obtain the name
            if(name.contains("-")) {
                nameWithNumber = name.substring(name.lastIndexOf("-")+1,name.length());
                System.out.println("name with number " + nameWithNumber);
                name = name.substring(0, name.lastIndexOf("-"));
                System.out.println("This is the current name " +PracticeMenuController.getCurrentName());
                multipleNameIndex = Integer.parseInt(nameWithNumber) -1;

            }

            System.out.println("this is the database recordings " + databaseList);

            System.out.println("This is a name " + name);
            String path = databaseList.get(nameList.indexOf(name) + (multipleNameIndex));
            System.out.println("This is path " + path);

            String pathToFile = "Database/" + name + "/Database-Recordings/" + path + ".wav";
            System.out.println("This is path to file  " + pathToFile);

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

    @FXML
    public void handleChangeButton() throws IOException {
        Main.changeSceneDataBase();
    }

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

    public void names(ObservableList<String> selectedNames, ArrayList<String> namesWithoutNumbersList,
                      ObservableList<String> namesWithNumbersList) {

        items = selectedNames;
        for (String names : namesWithoutNumbersList) {
            namesWithoutNumbers.add(names);
        }

        for (String names : namesWithNumbersList) {
            namesWithNumbers.add(names);
        }

        System.out.println("this is playing " + namesWithNumbersList);
        practiceList.setItems(items);
    }

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

            System.out.println("This is the current name " + PracticeMenuController.getCurrentName());
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    items.add(listOfFiles[i].getName());
                    System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }

            userCreations.setItems(items);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiceList.getSelectionModel().select(0);
        namesWithoutNumbers = new ArrayList<String>();
        namesWithNumbers = new ArrayList<String>();
        userRecordings = new ArrayList<String>();
        userRecordingsList = FXCollections.observableArrayList();
        currentName = "Name";
        practiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Practice list is clicked");
                userCreations.getSelectionModel().clearSelection();

                if (!(practiceList.getSelectionModel().isEmpty())) {
                    nameLabel.setText(namesWithoutNumbers
                            .get(namesWithNumbers.indexOf(practiceList.getSelectionModel().getSelectedItem())));
                }

                currentName = practiceList.getSelectionModel().getSelectedItem();
                System.out.println(currentName);
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

    @FXML
    public void userCreationsListClicked() {
        System.out.println("User list is clicked");

        practiceList.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleShuffleButton() {
        ObservableList<String> tempList = FXCollections.observableArrayList();
        List<String> praticeSelection = DataBaseController._practiceSelection;
        Collections.shuffle(praticeSelection);
        tempList.addAll(praticeSelection);
        practiceList.setItems(tempList);

    }

    public static String getCurrentName() {
        System.out.println("Current name" + currentName);
        return currentName;
    }

    public static String getCurrentNameWithoutNumber() {
        if (currentName.contains("-")) {
            currentName = currentName.substring(0, currentName.lastIndexOf("-"));
            System.out.println("This is the current name " + currentName);
        }
        return currentName;
    }

}