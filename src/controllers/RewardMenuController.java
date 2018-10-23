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


    @FXML
    public void handle10PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/applause_y.wav";

        PlayRecordings.handlingPlayingRecordings(pathToFile);

    }

    @FXML
    public void handle20PointButton() {

        String pathToFile = System.getProperty("user.dir") + "/Rewards/cheering.wav";

        PlayRecordings.handlingPlayingRecordings(pathToFile);

    }

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

    public int getPoints(){
        return points;
    }

    public static void getRewardPoint() {
        File userRecordingDir = new File(System.getProperty("user.dir") + "/User-Recordings");
        points = userRecordingDir.list().length;
    }

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