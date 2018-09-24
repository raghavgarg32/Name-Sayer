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
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Timer;

/**
 * Controller for the recording scene
 */
public class RecordView implements Initializable {

    private Service<Void> _backgroundThread;

    private static Timer progressTimer = new Timer();

    private ArrayList<String> allUserRecordings;

    @FXML
    private Button micTestBtn;

    @FXML
    public Button backBtn;

    @FXML
    public Button backButton;


    @FXML
    private ProgressBar recordBar;

    @FXML
    private Label currentNameLabel;

    @FXML
    private Label recordLabel;

    private String currentName;

    private static String numberOfRecordings;

    /**
     * Callback function for the Mic test button, it changes the scene
     */
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
        currentNameLabel.setText(currentName);
    }

    /**
     * call back function for the record button that starts the recording processes of the application
     * @throws IOException
     */
    @FXML
    public void handleRecordButton() throws IOException {
        backButton.setVisible(false);
        progressTimer.cancel();
        recordBar.setProgress(0.0);
        recordLabel.setText("Audio is currently being recorded 5 seconds now");

        //Create a timer task for the progress bar
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                double progress = recordBar.getProgress();
                if (progress == 1) {
                    recordBar.setProgress(0);
                    progressTimer.cancel();
                    return;
                }

                recordBar.setProgress(progress + 0.01);
            }
        };

        progressTimer = new Timer();
        progressTimer.scheduleAtFixedRate(timerTask, 0, 50);
        gettingNumberOfUserRecordings();
        if (numberOfRecordings == null){
            numberOfRecordings = "";
        }
        //Use a background thread for recording
        _backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {

                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        ProcessBuilder recordBuilder = new ProcessBuilder("ffmpeg","-y","-f","alsa","-ac","1"
                                ,"-ar","44100","-i","default","-t", "5","./Database/"+currentName+"/User-Recordings/temp.wav");
                        try {
                            Process p = recordBuilder.start();
                            p.waitFor();

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };

        _backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            //Change scene when recording is finished
            @Override
            public void handle(WorkerStateEvent event) {
                Main.changeSceneConfirm();

            }
        });
        _backgroundThread.start();
    }

    /**
     * Helper method for initializing the record view scene
     */
    public void initScene(){
        recordLabel.setText("Press record to have your voice recorded");
        backButton.setVisible(true);
        progressTimer.cancel();
        recordBar.setProgress(0.0);

    }
    
    /**
     * Callback function for the back button which changes the scene back to the practice menu
     */
    @FXML
    public void handleBackBtn() {

        if(_backgroundThread == null) {
            recordLabel.setText("Press record to have your voice recorded");
            deleteRecording();
            Main.changeScenePractice();
            return;
        }
        //Check if a background thread is running
        if(_backgroundThread.isRunning()) {

            progressTimer.cancel();
            _backgroundThread.cancel();
            deleteRecording();
        }
        recordLabel.setText("Press record to have your voice recorded");
        Main.changeScenePractice();
    }

    /**
     * Help method to delete the temp recordings 
     */
    public void deleteRecording(){
        SwingWorker<Void,Void> deleteWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                currentName = PracticeMenuController.getCurrentNameWithoutNumber();
                String number = RecordView.getNumberOfRecordings();
                try {
                    Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+
                            "/Database/" + currentName + "/User-Recordings/temp.wav"));
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        };
        deleteWorker.execute();
    }
    
    /**
     * Helper method to get the number of user recordings for a specific database recording
     */
    public void gettingNumberOfUserRecordings(){
        SwingWorker gettingRecordingsNumberWorker = new SwingWorker<ArrayList<String>, Integer>() {

            @Override
            protected ArrayList<String> doInBackground() throws Exception {
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
        gettingRecordingsNumberWorker.execute();
        

    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allUserRecordings = new ArrayList<String>();
        currentNameLabel.setText("Name");

    }

    /**
     * Helper method to get the number of recordings
     * @return
     */
    public static String getNumberOfRecordings() {
        return numberOfRecordings;
    }
}