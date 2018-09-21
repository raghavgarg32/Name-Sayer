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

    private static List<String> _practiceSelection = new ArrayList<>();


    private ArrayList<String> nameArrayList = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    private ObservableList<String> list;

    @FXML
    private Button practiceBtn;


    @FXML
    public void handlePracticeBtn() {
        if(_practiceSelection.size() > 1) {
            Alert randomizeAlert = new Alert(Alert.AlertType.INFORMATION, "Would you like" +
                    " to randomise your selection",ButtonType.NO, ButtonType.YES);
            randomizeAlert.showAndWait();
            if(randomizeAlert.getResult() == ButtonType.YES) {
                Collections.shuffle(_practiceSelection);
                randomizeAlert.close();
                Main.changeScenePractice();
            }
            else if(randomizeAlert.getResult() == ButtonType.NO) {
                randomizeAlert.close();
                Main.changeScenePractice();
            }
        }
        else if(_practiceSelection.size() == 1) {
       	 Main.changeScenePractice();
       }	
        else if(_practiceSelection.size() == 0) {
            Alert selectionAlert = new Alert(Alert.AlertType.INFORMATION, "Please select " +
                    "something" ,ButtonType.OK);
            selectionAlert.showAndWait();
            if(selectionAlert.getResult() == ButtonType.OK) {
                selectionAlert.close();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
    
    public static List<String> getPracticeList() {
    	return _practiceSelection;
    }



}
