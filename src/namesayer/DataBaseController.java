package namesayer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBaseController implements Initializable {

    public static List<String> _practiceSelection = new ArrayList<>();

    private Service<Void> _backgroundThread;

    private ArrayList<String> nameArrayList = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    private ObservableList<String> list;

    @FXML
    private Button practiceBtn;


    @FXML
    public void handlePracticeBtn() {
        List<String> selectedItems = _creationList.getSelectionModel().getSelectedItems();
        System.out.println(_practiceSelection);
        if(_practiceSelection.size() > 1) {
            Alert randomizeAlert = new Alert(Alert.AlertType.INFORMATION, "Would you like" +
                    " to randomise your selection",ButtonType.NO, ButtonType.YES);
            randomizeAlert.showAndWait();
            if(randomizeAlert.getAlertType().equals(ButtonType.YES)) {
                Collections.shuffle(_practiceSelection);
                randomizeAlert.close();
            }
            else if(randomizeAlert.getAlertType().equals(ButtonType.NO)) {
                randomizeAlert.close();
            }
        }
        else if(_practiceSelection.size() == 0) {
            Alert selectionAlert = new Alert(Alert.AlertType.INFORMATION, "Please select" +
                    "something" ,ButtonType.OK);
            selectionAlert.showAndWait();
            if(selectionAlert.getAlertType().equals(ButtonType.OK)) {
                selectionAlert.close();
            }
        }
        Main.changeScenePractice();
    }


    // Trying to get this method to display .mp4 files in the Creations folder but its not working for some reason?
    public void listCreations() {
        File dir = new File("Creations/");
        List<String> fileList = new ArrayList<>();
        File[] directoryListing = dir.listFiles();
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".mp4");
            }
        };
        //Loop through files and store .mp4 files in a list
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(fileFilter.accept(dir, child.getName())) {
                    fileList.add(child.getName());
                }
            }
        }
        ObservableList<String> items = FXCollections.observableArrayList(fileList);
        _creationList.setItems(items);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listCreations();
        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //this code gets the check boxes but i dont know how to  work with it
        _creationList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty itemState = new SimpleBooleanProperty();
                itemState.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected == true) {
                        _practiceSelection.add(item);
                    }
                    else if(isNowSelected == false) {
                        _practiceSelection.remove(item);
                    }
                });
                return itemState;
            }
        }));


        // This is just test data for the list
        list = FXCollections.observableArrayList();
        _creationList.setItems(list);
        gettingRecordings();
    }

    public void gettingRecordings(){
        //This swingworker gets all of the creations from the NameSayer directory
        SwingWorker gettingRecordingsWorker = new SwingWorker<ArrayList<String>, Integer>() {

            @Override
            protected ArrayList<String> doInBackground() throws Exception {
                ArrayList<String> nameList = new ArrayList<String>();

                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "cd Database; ls -1 *.wav | sed -e 's/\\..*$//'");
                    Process process = builder.start();

                    InputStream stdout = process.getInputStream();
                    BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                    String line = null;

                    while ((line = stdoutBuffered.readLine()) != null) {
                        line = line.substring(line.lastIndexOf('_') + 1);
                        nameArrayList.add(line);

                        if (list.contains(line)){
                            System.out.println(line);
                            int occurrences = Collections.frequency(nameArrayList, line);
                            System.out.println(occurrences);
                            line =  line + '-' + occurrences;
                        }
                        list.add(line);
                    }
                    stdoutBuffered.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return null;
            }

        };
        gettingRecordingsWorker.execute();
    }



}
