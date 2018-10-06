package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button practiceBtn;

    @FXML
    private Button quitBtn;

    /**
     * Changes the scene to database when called
     */
    @FXML
    public void handlePracticeBtn() {
        Main.changeSceneDataBase();
    }

    /**
     * Exits the application when called
     */
    @FXML
    public void handleQuitBtn() {
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BashCommandWorker creationDirectoryWorker = new BashCommandWorker("if [ ! -e \"BadRecordingList.txt\" ]; then\n" +
                "    touch BadRecordingList.txt\n" +
                "fi");




        File antony = new File("antony.wav");
        File catherine = new File("catherine.wav");


    }

    /**
     * Method will take in a list of .wav files and concatenat them into one .wav file and return it
     *
     * @param listOfFiles
     * @return concat audio file
     */
    public File createConcatFile(List<File> listOfFiles) {

        String command = "ffmpeg -y";

        for(File file:listOfFiles) {
            removeWhiteNoise(file);
            String filename = file.getPath().substring(0, file.getPath().lastIndexOf('.'));

            ProcessBuilder noiseBuilder = new ProcessBuilder("/bin/bash","-c","ffmpeg -y -i "+file.getPath() + " -filter:a \"volume=0.5\" " +
                    filename + "Temp.wav");
            Process noiseProcess;
            try {
                noiseProcess = noiseBuilder.start();
                noiseProcess.waitFor();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            command =  command + " -i " + filename +"Temp.wav";
        }

        command = command + " -filter_complex '[0:0][1:0]concat=n="+ listOfFiles.size()+":v=0:a=1[out]' -map '[out]' concatFile.wav";

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

        return new File("concatFile.wav");
    }

    public void removeWhiteNoise(File file) {
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
