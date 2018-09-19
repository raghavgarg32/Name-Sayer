package namesayer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for the recording scene
 */
public class RecordView {
    @FXML
    Label recordLabel;

    @FXML
    public void handleRecordButton() throws IOException {
        recordLabel.setText("Recording now...");
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                Platform.runLater(new Runnable() {
                    public void run() {
                        recordingEnd();
                        Main.changeSceneConfirm();
                    }
                });            }

        };
        timer.schedule(task,5000l);

    }

    public void recordingEnd(){
        recordLabel.setText("Record DONE");

    }
}
