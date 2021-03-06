package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import helpers.PlayRecordings;
import helpers.SideButtons;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This is the controller for the Reward scene and handles the reward system
 */
public class RewardMenuController extends SideButtons implements Initializable {

    private static int points;

    @FXML
    private Label pointCounter;

    @FXML
    private Button tenPointBtn;

    @FXML
    private Button twentyPointBtn;

    @FXML
    private Button thirtyPointBtn;

    /**
     * Callback function for the 10 point button, will play a special .wav file
     */
    @FXML
    public void handle10PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/applause_y.wav";

        PlayRecordings.handlingPlayingRecordings(pathToFile);

    }

    /**
     * Callback function for the 20 point button, will play a special .wav file
     */
    @FXML
    public void handle20PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/cheering.wav";

        PlayRecordings.handlingPlayingRecordings(pathToFile);

    }

    /**
     * Callback function for the 30 point button, will play a special .wav file
     */
    @FXML
    public void handle30PointButton() {
        String pathToFile = System.getProperty("user.dir") + "/Rewards/car_crash2.wav";

        PlayRecordings.handlingPlayingRecordings(pathToFile);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File userRecordingDir = new File(System.getProperty("user.dir") + "/User-Recordings");
        if(userRecordingDir.exists()) {
            points = userRecordingDir.list().length;
        }


        updateButtonStatus();

    }

    /**
     * Returns the number of points
     * @return
     */
    public int getPoints(){
        return points;
    }

    /**
     * Reads how many recordings the user has created and then allocates them points accordingly
     */
    public static void getRewardPoint() {
        File userRecordingDir = new File(System.getProperty("user.dir") + "/User-Recordings");
        points = userRecordingDir.list().length;
    }

    /**
     * Update the label and button statuses of the scene depending on the number of points the user has
     */
    public void updateButtonStatus() {
        pointCounter.setText("You have " + points + " points");

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