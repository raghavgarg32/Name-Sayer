package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class DataBaseController implements Initializable {

    public static List<String> _practiceSelection = new ArrayList<>();


    private static ArrayList<String> nameArrayList = new ArrayList<>();

    private static List<String> databaseList = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    @FXML
    private ListView<String> playList;

    private static ObservableList<String> list;

    @FXML
    public TextField userName;

    @FXML
    private Button practiceBtn;

    private static String currentName;


    /**
     * This is a callback function  thats called when the practice button is called, it changes the scene when the
     * callback function is called
     */
    @FXML
    public void handlePracticeBtn() {
        getDatabaseList();

        Main.changeScenePractice();
    }

    @FXML
    public void uploadPlayList(){
        List<String> fileNames = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            System.out.println(selectedFile.getAbsolutePath());
            try {
                File file = new File(selectedFile.getAbsolutePath());
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    fileNames.add(line);
                }
                fileReader.close();
                if (!list.containsAll(fileNames)){
                    Alert alert = new Alert(Alert.AlertType.NONE, "This text file contains names that aren't in the database, those names will not be added" + "to review", ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        alert.close();
                    }
                }
                    for (String name : fileNames) {
                        String[] individualNames = name.split(" ");
                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(individualNames));
                        System.out.println("Thsi is " + arrayList);
                        if (list.containsAll(arrayList)) {
                            playList.getItems().add(name);
                        }
                    }
//                } else {

//                    }
//                }


                System.out.println("Contents of file:");
                System.out.println(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @FXML
    public void addToPlayList(){
        if (userName.getText().length() > 0) {
            playList.getItems().add(userName.getText());
            _practiceSelection.add(userName.getText());

            userName.clear();
        }

    }

    public ObservableList<String> getSelectedNames(){
        return playList.getItems();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BashCommandWorker settingUpDatabaseWorker = new BashCommandWorker("cd Database;\n" +
                "rename 'y/A-Z/a-z/' *\n" +
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




        // This is just test data for the list
        list = FXCollections.observableArrayList();
        _creationList.setItems(list);
        gettingRecordings();


        _creationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //Changes the practice view depending on which was the last selected list
            @Override
            public void handle(MouseEvent event) {
                currentName = _creationList.getSelectionModel().getSelectedItem();
                userName.setText((userName.getText()+ " " + currentName).trim());
                System.out.println("This is the name of the customer " + currentName);
            }
        });
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

                        if (!list.contains(line)){
                            list.add(line);
                        }

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
            System.out.println("This is the name " + name);
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
        Collections.shuffle(databaseList);
        System.out.println("This is database: " + databaseList);
        ArrayList<Integer> temp = new ArrayList<>();
        for (String fileName : nameArrayList){
            int occurrences = Collections.frequency(nameArrayList, fileName);
            temp.add(occurrences);
        }

        System.out.println("This is temp: " + temp);

        ArrayList<String> tempArrayList = new ArrayList<>();

        for (int i = 0; i < temp.size() - 1; i++) {
            if (((!(temp.get(i) == temp.get(i+1))) && (temp.get(i) > 1)) || (temp.get(i) == 1)){
                tempArrayList.add(databaseList.get(i));
            }

        }

        System.out.println("This is database: " + tempArrayList);
        System.out.println("This is database: " + databaseList);
        return tempArrayList;
    }

    /**
     * Callback function which is called when the back button,it changes the scene when called
     */
    @FXML
    public void handleBackButton() {
        Main.changeSceneMain();
    }
}
