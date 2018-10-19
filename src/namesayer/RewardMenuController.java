package namesayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.SwingWorker;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RewardMenuController extends SideButtons implements Initializable {

    private static int points;

    private Task<Void> _playWorker;
    
    private Thread playThread;

    @FXML
    private Label pointCounter;

    @FXML
    private Button tenPointBtn;

    @FXML
    private Button twentyPointBtn;

    @FXML
    private Button thirtyPointBtn;


    @FXML
    public void handle10PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/applause_y.wav";

        _playWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception  {
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
        playThread = new Thread(_playWorker);
        playThread.start();

    }

    @FXML
    public void handle20PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/cheering.wav";


        _playWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
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
        playThread = new Thread(_playWorker);
        playThread.start();

    }

    @FXML
    public void handle30PointButton() {
        String pathToFile = System.getProperty("user.dir") + "/Rewards/car_crash2.wav";

        _playWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
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
        playThread = new Thread(_playWorker);
        playThread.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File userRecordingDir = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(userRecordingDir.exists()) {
            points = userRecordingDir.list().length;
        }


        updateButtonStatus();

    }

    public int getPoints(){
        return points;
    }

    public static void getRewardPoint() {
        File userRecordingDir = new File(System.getProperty("user.dir") + "/User-Recordings");
        points = userRecordingDir.list().length;
    }

    public void updateButtonStatus() {
        pointCounter.setText("You have " + points + " number of points");

        if(points < 10 ) {
            tenPointBtn.setDisable(true);
            twentyPointBtn.setDisable(true);
            thirtyPointBtn.setDisable(true);
        }
        else if(points >= 10 && points < 20) {
            tenPointBtn.setDisable(false);
            twentyPointBtn.setDisable(true);
            thirtyPointBtn.setDisable(true);
        }
        else if(points >= 20 && points < 30) {
            tenPointBtn.setDisable(false);
            twentyPointBtn.setDisable(false);
            thirtyPointBtn.setDisable(true);
        }
        else if(points >= 30) {
            tenPointBtn.setDisable(false);
            twentyPointBtn.setDisable(false);
            thirtyPointBtn.setDisable(false);
        }
    }

}