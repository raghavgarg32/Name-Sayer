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

/**
 * Sets up the database logic, allows to select to practice
 */
public class DataBaseController implements Initializable {

    public static List<String> _practiceSelection = new ArrayList<>();
    private static ArrayList<String> nameArrayList = new ArrayList<>();
    private static List<String> databaseList = new ArrayList<>();
    private static ObservableList<String> list;

    @FXML
    private ListView<String> _creationList;

    @FXML
    private Button practiceBtn;

    /**
     * handles practice button and makes sure that the user has selected names
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

    /**
     * Sets up the folder for the database and the checkboxes
     * @param location
     * @param resources
     */
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

        _creationList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty itemState = new SimpleBooleanProperty();
                itemState.addListener((obs, wasSelected, isNowSelected) -> {
                    if (!(_practiceSelection.contains(item)) || isNowSelected == true ) {
                        _practiceSelection.add(item);

                    }
                    else if(isNowSelected == false) {
                        System.out.println("the item has been removed");
                        _practiceSelection.remove(item);
                    }
                });
                return itemState;
            }
        }));
        list = FXCollections.observableArrayList();
        _creationList.setItems(list);
        gettingRecordings();
    }

    /**
     * This gets all of the recordings to show them to the user and adds incremental numbers to duplicates
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

    /**
     * Returns the names that have been selected as Observable List
     * @return
     */
    public static ObservableList<String> getItemList(){
        ObservableList<String> items =FXCollections.observableArrayList ();
        for (String name : _practiceSelection){
            System.out.println(name);
            items.add(name);
        }

        return items;
    }

    /**
     * Returns the list of all names without numbers
     * @return
     */
    public static ArrayList<String> getNamesWithoutNumbers(){
        System.out.println("Array list  " + nameArrayList);

        return nameArrayList;
    }

    /**
     * Returns the list of all names with numbers
     * @return
     */
    public static ObservableList<String> getNamesWithNumbers(){
        System.out.println("Array list  " + list);

        return list;
    }

    /**
     * Returns the list of all names with their database recording name
     * @return
     */
    public static List<String> getDatabaseList() {
    	return databaseList;
    }
}
