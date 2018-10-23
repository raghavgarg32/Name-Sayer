package controllers;

import helpers.BashCommandWorker;
import helpers.MakeHeadingNameFit;
import helpers.SideButtons;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Controller for the recording scene
 */
public class RecordView extends SideButtons implements Initializable {


    @FXML
    private Button micTestButton;

    @FXML
    public Button backButton;

    private static Thread recordThread;

    private static Task<Void> recordTask;

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

    private static String recordingLocation = "User-Recordings";

    public static void recordingForNewDBRecording(){
        isThisNewDBRecording = true;
        recordingLocation = "Database";

    }

    public static void recordingForUserRecording(){
        isThisNewDBRecording = false;
        recordingLocation = "User-Recordings";
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

        currentNameLabel.setText(MakeHeadingNameFit.changeName(currentNameSelected));
    }

    /**
     * call back function for the record button that starts the recording processes of the application
     * @throws IOException
     */
    @FXML
    public void handleRecordButton() throws IOException {
        micTestButton.setVisible(false);

        recordTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder recordBuilder = new ProcessBuilder("ffmpeg","-y","-f","alsa","-ac","1"
                        ,"-ar","44100","-i","default","./"+recordingLocation+"/temp.wav");
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
            micTestButton.setVisible(true);
            recordBar.setProgress(0.0);
            stopRecording();
            if (isThisNewDBRecording){
                Main.changeSceneConfrimDBRecordingsMenu();
            } else {
                Main.changeSceneConfirm();
            }
            return;
        }
        else {
            recordThread.start();
            recordBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            this.recordButton.setText("Stop");
        }

        backButton.setVisible(false);

        gettingNumberOfUserRecordings();

        if (numberOfRecordings == null){
            numberOfRecordings = "";
        }


    }

    /**
     * Helper method for initializing the record view scene
     */
    public void initScene(){
        backButton.setVisible(true);
        if (isThisNewDBRecording){
            backButton.setText("Back");
        } else {
            backButton.setText("Practice Menu");

        }
        recordLabel.setText("Press record to have your voice recorded");
        recordBar.setProgress(0.0);
        micTestButton.setVisible(true);

    }

    /**
     * Callback function for the back button which changes the scene back to the practice menu
     */
    @FXML
    public void handleBackBtn() {

        recordLabel.setText("Press record to have your voice recorded");
        deleteRecording();
        if (isThisNewDBRecording) {
            Main.changeSceneAddDBRecordingsMenu();
        } else {
            Main.changeScenePractice();
        }
    }

    /**
     * Help method to delete the temp recordings
     */
    public void deleteRecording(){
        Task<Void> deleteWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception{
                currentName = PracticeMenuController.getCurrentName(false);
                String number = RecordView.getNumberOfRecordings();
                try {
                    Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+"/"+recordingLocation+"/temp.wav"));
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(deleteWorker).start();;
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
                            "cd "+recordingLocation+";\n" +
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

    public static void stopRecording() {
        BashCommandWorker stopBuilder = new BashCommandWorker("killall ffmpeg");
        if(recordThread != null && recordTask != null) {
            recordThread.interrupt();
            recordTask.cancel();
        }
    }
}