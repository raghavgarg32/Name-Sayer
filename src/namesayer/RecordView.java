package namesayer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Timer;

/**
 * Controller for the recording scene
 */
public class RecordView extends SideButtons implements Initializable {

    private Service<Void> _backgroundThread;

    private static Timer progressTimer = new Timer();

    private ArrayList<String> allUserRecordings;

    @FXML
    private Button micTestBtn;

    @FXML
    public Button backBtn;

    @FXML
    public Button backButton;

    private Thread recordThread;

    private Task<Void> recordTask;

    @FXML
    public Button recordButton;

    @FXML
    private ProgressBar recordBar;

    @FXML
    private Label currentNameLabel;

    @FXML
    private Label recordLabel;

    private String currentName;

    private static String numberOfRecordings;

    private static Boolean isThisNewDBRecording = false;

    private static String recordingLocation = "./User-Recordings/";

    public static void recordingForNewDBRecording(){
        isThisNewDBRecording = true;
        recordingLocation = "./Database/";
    }

    public static void recordingForUserRecording(){
        isThisNewDBRecording = false;
        recordingLocation = "./User-Recordings/";
    }

    @FXML
    public void handleMicTestButton() {
        Main.changeSceneMicTest();
    }

    /**
     * getter method for getting the name for the current selected name
     * @param currentNameSelected
     */
    public void getNameForRecording(String currentNameSelected){
        currentName = currentNameSelected;
        if (currentName.length() > 20){
            currentName = currentName.substring(0,17);
            currentName = currentName + "...";
        }
        currentNameLabel.setText(currentName);
    }

    /**
     * call back function for the record button that starts the recording processes of the application
     * @throws IOException
     */
    @FXML
    public void handleRecordButton() throws IOException {

    	micTestBtn.setVisible(false);

        recordTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder recordBuilder = new ProcessBuilder("ffmpeg","-y","-f","alsa","-ac","1"
                        ,"-ar","44100","-i","default","./User-Recordings/temp.wav");
                try {
                    Process recordProcess = recordBuilder.start();

                } catch (IOException e1) {

                    e1.printStackTrace();
                }
                return null;
            }
        };

        recordThread = new Thread(recordTask);

        if(this.recordButton.getText() == "Stop") {
            this.recordButton.setText("Record");
            recordBar.setProgress(0.0);
            stopRecording();
            Main.changeSceneConfirm();
            return;
        }
        else {
            recordThread.start();
            recordBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            this.recordButton.setText("Stop");
        }

        backButton.setVisible(false);
        progressTimer.cancel();

        gettingNumberOfUserRecordings();

        if (numberOfRecordings == null){
            numberOfRecordings = "";
        }


    }

    /**
     * Helper method for initializing the record view scene
     */
    public void initScene(){
        recordLabel.setText("Press record to have your voice recorded");
        recordButton.setText("Record");
        backButton.setVisible(true);
        micTestBtn.setVisible(true);
        recordBar.setProgress(0.0);
    }

    /**
     * Callback function for the back button which changes the scene back to the practice menu
     */
    @FXML
    public void handleBackBtn() {

        if(recordThread == null) {
            recordLabel.setText("Press record to have your voice recorded");
            deleteRecording();
            Main.changeScenePractice();
            return;
        }

        //Check if a background thread is running
        if(recordThread.isAlive()) {
            stopRecording();
            deleteRecording();
        }
        recordLabel.setText("Press record to have your voice recorded");
        Main.changeScenePractice();
    }

    /**
     * Help method to delete the temp recordings
     */
    public void deleteRecording(){
        Task<Void> deleteWorker = new Task<Void>() {
            @Override
            protected Void call() throws Exception{
                currentName = PracticeMenuController.getCurrentNameWithoutNumber(false);
                String number = RecordView.getNumberOfRecordings();
                try {
                    Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+"/User-Recordings/temp.wav"));
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(deleteWorker);
    }

    /**
     * Helper method to get the number of user recordings for a specific database recording
     */
    public void gettingNumberOfUserRecordings(){
        Task<Void> gettingRecordingsNumberWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                ArrayList<String> nameList = new ArrayList<String>();

                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "cd Database;\n" +
                            "cd "+currentName+";\n" +
                            "cd User-Recordings;\n" +
                            "\n" +
                            "echo $(ls -l | wc -l)");
                    Process process = builder.start();

                    InputStream stdout = process.getInputStream();
                    BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

                    String line = null;

                    while ((line = stdoutBuffered.readLine()) != null) {
                        numberOfRecordings = line;


                    }
                    stdoutBuffered.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return null;
            }

        };
        new Thread(gettingRecordingsNumberWorker).start();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allUserRecordings = new ArrayList<String>();
        currentNameLabel.setText("Name");
        recordBar.setProgress(0.0);
        

    }

    /**
     * Helper method to get the number of recordings
     * @return
     */
    public static String getNumberOfRecordings() {
        return numberOfRecordings;
    }

    public void stopRecording() {
        BashCommandWorker stopBuilder = new BashCommandWorker("killall ffmpeg");
        recordThread.interrupt();
        recordTask.cancel();
    }
}