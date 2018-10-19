package namesayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class DataBaseController extends SideButtons implements Initializable {

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

    private List<String> databaseNames;

    @FXML
    private Button practiceBtn;

    private static String currentName;

    private static HashMap<String, String> Names = new HashMap<>();

    private static HashMap<String, String> actualNames = new HashMap<>();

    ArrayList<String> actualNamesBadRecordings = new ArrayList<>();

    private List<String> nameList;



    @FXML
    private Button closeButton;

    public static HashMap<String, String> getNamesHashMap(){
        return Names;
    }

    @FXML
    private ImageView homeImage;

    /**
     * This is a callback function  thats called when the practice button is called, it changes the scene when the
     * callback function is called
     */
    @FXML
    public void handlePracticeBtn() {
        if (!_practiceSelection.isEmpty()) {
            getDatabaseList();

            Main.changeScenePractice();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plesase add names to your playlist.", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
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
                    fileNames.add(line.toLowerCase());
                }
                fileReader.close();
                if (!list.containsAll(fileNames)){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "This text file contains names that aren't in the database, those names will not be added" + "to review", ButtonType.OK);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        alert.close();
                    }
                }
                for (String name : fileNames) {
                    String[] individualNames = name.split(" ");
                    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(individualNames));
                    if (list.containsAll(arrayList)) {
                        playList.getItems().add(name);
                        _practiceSelection.add(name);


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

    public ArrayList<String> getBadRecordings(){
        Scanner s = null;
        try {
            s = new Scanner(new File("BadRecordingList.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> badlist = new ArrayList<String>();
        while (s.hasNext()){
            badlist.add(s.next());
        }
        s.close();

        return badlist;
    }

    @FXML
    public void addToPlayList(){
        if (userName.getText().length() > 0) {
            String[] singleName = userName.getText().split(" ");
            String tempDatabaseName = "";

            Boolean nameExists = true;

            for ( String ss : singleName) {
                System.out.println("SSSSS  " + ss);
                for (String database  : databaseList){
                    if (!nameArrayList.contains(ss)){

                        nameExists = false;
                    }
                }
            }
            if (nameExists){
                playList.getItems().add(userName.getText());
                _practiceSelection.add(userName.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "This name doesn't exist in our database, please add another name.", ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
            }
            userName.clear();
        }

    }

    public ObservableList<String> getSelectedNames(){
        return playList.getItems();
    }

    @FXML
    public void handleQuitButton(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleExportPlayListButton(){
        for (String name : _practiceSelection){
            BashCommandWorker creationDirectoryWorker = new BashCommandWorker("name='"+name+"'\n" +
                    "\n" +
                    "if ! grep -qF \"$name\" UserPlayList.txt ; then " +
                    "echo \"$name\" >> UserPlayList.txt ; " +
                    "fi");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "DONE! - check in local folder", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseNames = new ArrayList<String>();
        BashCommandWorker settingUpDatabaseWorker = new BashCommandWorker(
                "mkdir User-Recordings;\n" + "mkdir Concat-Recordings;\n" + "cd Database;\n" + "rename 'y/A-Z/a-z/' *\n"
                        + "\n" + "for i in $(ls); do\n" + "\n" + "names=$(echo $i | awk -F\"_\" '{print $NF}')\n"
                        + "nameWithourExtension=\"${names%.*}\"\n" + "    echo \"This is i $i \"\n"
                        + "    mkdir \"$nameWithourExtension\"\n" + "    \n" + "    cd \"$nameWithourExtension\"\n"
                        + "    \n" + "    mkdir \"Database-Recordings\"\n" + "    \n"
                        + "    mkdir \"User-Recordings\"\n" + "\n" + "    mkdir \"Ratings\"\n" + "    \n"
                        + "    mv \"../$i\" \"./Database-Recordings\"\n" + "\n" + "    cd ..\n" + "\n" + "done\n");

        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // This is just test data for the list
        list = FXCollections.observableArrayList();
        _creationList.setItems(list);
        gettingRecordings();
        selectGoodRecordings();


        _creationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            // Changes the practice view depending on which was the last selected list
            @Override
            public void handle(MouseEvent event) {
                String output;
                nameList = new ArrayList<String>();
                currentName = _creationList.getSelectionModel().getSelectedItem();

                if (userName.getText().isEmpty() || userName.getText() == null
                        || userName.getText().charAt(userName.getText().length() - 1) == ' ') {
                    userName.setText(userName.getText() + currentName);
                }

                String[] namesInTextField = userName.getText().trim().split(" ");

                for (int i = 0; i < namesInTextField.length; i++) {
                    nameList.add(namesInTextField[i]);
                }

                if (!nameArrayList.contains(nameList.get(nameList.size() - 1))) {
                    nameList.remove(nameList.size() - 1);
                    nameList.add(currentName);
                }

                output = String.join(" ", nameList) + " ";

                userName.setText(output);
                System.out.println("This is the name of the customer " + currentName);
            }
        });

        userName.textProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

                if (userName.getText() == null || userName.getText().isEmpty() || !userName.getText().contains(" ")) {
                    search((String) userName.getText().trim());
                } else {
                    System.out.println(userName.getText().substring(userName.getText().lastIndexOf(" "),
                            userName.getText().length()));
                    search((String) userName.getText()
                            .substring(userName.getText().lastIndexOf(" "), userName.getText().length()).trim());
                }

            }

        });
    }

    @FXML
    public void search(String nameToSearch) {
        List<String> foundList = new ArrayList<String>();
        List<String> notFoundList = new ArrayList<String>();

        if (userName.getText() != null && (nameToSearch.isEmpty())) {
            _creationList.setItems(list);
        }

        nameToSearch = nameToSearch.toLowerCase();
        ObservableList<String> finalList = FXCollections.observableArrayList();

        for (String name : nameArrayList) {
            boolean match = true;
            if (!(name.contains(nameToSearch))) {
                match = false;
                notFoundList.add(name);
            }
            if (match) {
                foundList.add(name);
            }
        }

        finalList.addAll(foundList);
        finalList.addAll(notFoundList);

        _creationList.setItems(finalList);
    }


//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        databaseNames = new ArrayList<String>();
//        BashCommandWorker settingUpDatabaseWorker = new BashCommandWorker("mkdir User-Recordings;\n" +
//                "mkdir Concat-Recordings;\n" +
//                "cd Database;\n" +
//                "rename 'y/A-Z/a-z/' *\n" +
//                "\n" +
//                "for i in $(ls); do\n" +
//                "\n" +
//                "names=$(echo $i | awk -F\"_\" '{print $NF}')\n" +
//                "nameWithourExtension=\"${names%.*}\"\n" +
//                "    echo \"This is i $i \"\n" +
//                "    mkdir \"$nameWithourExtension\"\n" +
//                "    \n" +
//                "    cd \"$nameWithourExtension\"\n" +
//                "    \n" +
//                "    mkdir \"Database-Recordings\"\n" +
//                "    \n" +
//                "    mkdir \"User-Recordings\"\n" +
//                "\n" +
//                "    mkdir \"Ratings\"\n" +
//                "    \n" +
//                "    mv \"../$i\" \"./Database-Recordings\"\n" +
//                "\n" +
//                "    cd ..\n" +
//                "\n" +
//                "done\n");
//
//        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//
//
//        // This is just test data for the list
//        list = FXCollections.observableArrayList();
//        _creationList.setItems(list);
//        gettingRecordings();
//        selectGoodRecordings();
//
//
//        _creationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            //Changes the practice view depending on which was the last selected list
//            @Override
//            public void handle(MouseEvent event) {
//                currentName = _creationList.getSelectionModel().getSelectedItem();
//                userName.setText((userName.getText()+ " " + currentName).trim());
//                System.out.println("This is the name of the customer " + currentName);
//            }
//        });
//
//    }



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
                            "ls -1 *.wav\n" +
                            "cd ..\n" +
                            "cd ..\n" +
                            "\n" +
                            "done");
                    Process process = builder.start();

                    InputStream stdout = process.getInputStream();
                    BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                    String line = null;

                    while ((line = stdoutBuffered.readLine()) != null) {
                        String databaseName = line;
                        databaseList.add(line);
//                        System.out.println("This is the recording file " +line);
                        line = line.substring(0,line.length()-4);
                        line = line.substring(line.lastIndexOf('_') + 1);
                        nameArrayList.add(line);
//                        System.out.println("This is the ds file " +line);
                        list.add(line);



                    }
                    stdoutBuffered.close();
                    ArrayList<String> namesBadRecordings = new ArrayList<>();
                    System.out.println("This is the database list ");
                    for (String actualName : getBadRecordings()){
                        actualName = actualName.substring(0,actualName.length()-4);
                        actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
                        namesBadRecordings.add(actualName);
                        System.out.println("acsdfdsfastas " + actualName);
                    }
                    System.out.println(namesBadRecordings);

                    for (String databaseName : databaseList){
//            System.out.println("This is the database list " + databaseName);
                        String actualName = databaseName.substring(0,databaseName.length()-4);
                        actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
//            System.out.println("This is the actual name " + actualName);
                        int occurrences = Collections.frequency(nameArrayList, actualName);
                        System.out.println("Occurence " + occurrences);
                        System.out.println("actas " + Collections.frequency(namesBadRecordings, actualName));

                        if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))){
                            if (!getBadRecordings().contains(databaseName)){
                                Names.put(actualName, databaseName);
                            }
                        } else {
                            Names.put(actualName, databaseName);

                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return null;
            }

        };
        gettingRecordingsWorker.execute();
    }

    public ArrayList<String> actualNamesInBadRecordings(){
        for (String actualName : getBadRecordings()){
            actualNamesBadRecordings.add(actualName);
        }
        return actualNamesBadRecordings;
    }

    public void selectGoodRecordings(){
        ArrayList<String> namesBadRecordings = new ArrayList<>();
        System.out.println("This is the database list ");
        for (String actualName : getBadRecordings()){
            actualName = actualName.substring(0,actualName.length()-4);
            actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
            namesBadRecordings.add(actualName);
            System.out.println("acsdfdsfastas " + actualName);
        }

        System.out.println(namesBadRecordings);

        for (String databaseName : databaseList){
//            System.out.println("This is the database list " + databaseName);
            String actualName = databaseName.substring(0,databaseName.length()-4);
            actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
//            System.out.println("This is the actual name " + actualName);
            int occurrences = Collections.frequency(nameArrayList, actualName);
            System.out.println("Occurence " + occurrences);
            System.out.println("actas " + Collections.frequency(namesBadRecordings, actualName));

            if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))){
                if (!getBadRecordings().contains(databaseName)){
                    actualNames.put(actualName, databaseName);
                }
            } else {
                actualNames.put(actualName, databaseName);

            }
        }
        Iterator it = actualNames.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (pair.getKey().equals("li")) {
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

    }

    public static void addingNewDBRecording(String databaseName, String realName){
        databaseList.add(databaseName);
        nameArrayList.add(realName);
        list.add(realName);
        Names.put(realName,databaseName);
    }

    /**
     * Helper method which gets the items from the _practiceSelection
     */
    public static ObservableList<String> getItemList(){
        ObservableList<String> items =FXCollections.observableArrayList ();
        for (String name : _practiceSelection){
            System.out.println("This is the name " + name);
            String[] individualNames = name.split(" ");
            if (name.contains(" ")) {
                List<File> listOfFiles = new ArrayList<>();
                for (String ss : individualNames) {
                    System.out.println("Individual names " + ss);
                    System.out.println("Database names " + Names.get(ss));

                    File antony = new File("./Database/" + ss + "/Database-Recordings/" + Names.get(ss));


                    listOfFiles.add(antony);

                }
                createConcatFile(listOfFiles, "./Concat-Recordings/" + name.replaceAll(" ", "_"));
            }
            items.add(name);
            System.out.println("Items " + items);
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
    public static ObservableList<String> getNames(){
        System.out.println(list);

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
     * Method will take in a list of .wav files and concatenat them into one .wav file and return it
     *
     * @param listOfFiles
     * @return concat audio file
     */
    public static File createConcatFile(List<File> listOfFiles, String name) {

        String command = "ffmpeg -y";

        for(File file:listOfFiles) {
            removeWhiteNoise(file);
            String filename = file.getPath().substring(0, file.getPath().lastIndexOf('.'));
            System.out.println(filename);
//            ProcessBuilder noiseBuilder = new ProcessBuilder("/bin/bash","-c","ffmpeg -y -i "+file.getPath() + " -filter:a \"volume=0.5\" " +
//                    file.getPath());
//            Process noiseProcess;
//            try {
//                noiseProcess = noiseBuilder.start();
//                noiseProcess.waitFor();
//            } catch (IOException | InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            command =  command + " -i " + file.getPath();
        }

        command = command + " -filter_complex '[0:0][1:0]concat=n="+ listOfFiles.size()+":v=0:a=1[out]' -map '[out]' "+name+".wav";

        System.out.println(command);

        ProcessBuilder concatBuilder = new ProcessBuilder("/bin/bash","-c",command);

        try {
            Process concatProcess = concatBuilder.start();
            concatProcess.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new File(name + ".wav");
    }

    public static void removeWhiteNoise(File file) {
        ProcessBuilder whitenoiseBuilder = new ProcessBuilder("/bin/bash","-c","ffmpeg -y -hide_banner -i " + file.getPath() + " -af " +
                "silenceremove=1:0:-35dB:1:5:-35dB:0" + file.getPath());

        try {
            Process whitenoiseProcess = whitenoiseBuilder.start();
            whitenoiseProcess.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}