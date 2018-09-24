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


    private static ArrayList<String> nameArrayList = new ArrayList<>();

    private static List<String> databaseList = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    private static ObservableList<String> list;

    @FXML
    private Button practiceBtn;


    /**
     * This is a callback function  thats called when the practice button is called, it changes the scene when the 
     * callback function is called 
     */
    @FXML
    public void handlePracticeBtn() {
        if (_practiceSelection.size() == 0) {
            Alert selectionAlert = new Alert(Alert.AlertType.NONE, "Please select" + " something", ButtonType.OK);
            selectionAlert.showAndWait();
            if (selectionAlert.getResult() == ButtonType.OK) {
                selectionAlert.close();
            }
        } else {
            Main.changeScenePractice();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BashCommandWorker settingUpDatabaseWorker = new BashCommandWorker("cd Database;\n" +
                "\n" +
                "for i in $(ls); do\n" +
                "\n" +
                "names=$(echo $i | awk -F\"_\" '{print $NF}')\n" +
                "nameWithourExtension=\"${names%.*}\"\n" +
                "    echo \"This is i $i \"\n" +
                "    mkdir \"$nameWithourExtension\"\n" +
                "    \n" +
                "    cd \"$nameWithourExtension\"\n" +
                "    \n" +
                "    mkdir \"Database-Recordings\"\n" +
                "    \n" +
                "    mkdir \"User-Recordings\"\n" +
                "\n" +
                "    mkdir \"Ratings\"\n" +
                "    \n" +
                "    mv \"../$i\" \"./Database-Recordings\"\n" +
                "\n" +
                "    cd ..\n" +
                "\n" +
                "done\n");

        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // Add checkboxes to the listview to allow the user to select mulitple options
        _creationList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty itemState = new SimpleBooleanProperty();
                itemState.addListener((obs, wasSelected, isNowSelected) -> {
                    if (!(_practiceSelection.contains(item)) || isNowSelected == true ) {
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
    
    /**
     * Helper method to read recordings from the database folder
     */
    public void gettingRecordings(){
        //This swingworker gets all of the creations from the NameSayer directory
        SwingWorker gettingRecordingsWorker = new SwingWorker<ArrayList<String>, Integer>() {

            @Override
            protected ArrayList<String> doInBackground() throws Exception {
                ArrayList<String> nameList = new ArrayList<String>();

                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "cd Database;\n" +
                            "\n" +
                            "for i in $(ls); do\n" +
                            "cd $i\n" +
                            "\n" +
                            "cd Database-Recordings\n" +
                            "\n" +
                            "ls -1 *.wav | sed -e 's/\\..*$//'\n" +
                            "cd ..\n" +
                            "cd ..\n" +
                            "\n" +
                            "done");
                    Process process = builder.start();

                    InputStream stdout = process.getInputStream();
                    BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                    String line = null;

                    while ((line = stdoutBuffered.readLine()) != null) {
                        databaseList.add(line);
                        line = line.substring(line.lastIndexOf('_') + 1);
                        nameArrayList.add(line);

                        if (list.contains(line)){
                            
                            int occurrences = Collections.frequency(nameArrayList, line);
                            
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

    /**
     * Helper method which gets the items from the _practiceSelection 
     */
    public static ObservableList<String> getItemList(){
        ObservableList<String> items =FXCollections.observableArrayList ();
        for (String name : _practiceSelection){
            items.add(name);
        }

        return items;
    }

    /**
     * Method which returns the names in the database without numbers
     */
    public static ArrayList<String> getNamesWithoutNumbers(){
        

        return nameArrayList;
    }

    
    /**
     * Method which returns the names in the database but with numbers
     * @return
     */
    public static ObservableList<String> getNamesWithNumbers(){
        

        return list;
    }

    /**
     * method which returns the databaselist 
     * @return
     */
    public static List<String> getDatabaseList() {
        return databaseList;
    }

    /**
     * Callback function which is called when the back button,it changes the scene when called
     */
    @FXML
    public void handleBackButton() {
        Main.changeSceneMain();
    }
}