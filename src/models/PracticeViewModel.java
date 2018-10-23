package models;

import controllers.*;
import helpers.Alerts;
import helpers.ConcateAudioFiles;
import helpers.PlayRecordings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the model for the practice view controller, handles playing the recording and recording
 */
public class PracticeViewModel {

    @FXML
    public Label names;

    String pathToFile;
    String name;
    List<File> listOfFiles = new ArrayList<File>();
    /**
     * Callback function which is called when the play button is pressed. It checks
     * which list is selected and plays the corresponding item
     *
     * @throws IOException
     */
    @FXML
    public void playDBRecording(ListView<String> practiceList) throws IOException {
        listOfFiles.clear();
        String selectedItem = practiceList.getSelectionModel().getSelectedItem().trim();
        
        if (practiceList.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection to play", ButtonType.OK,null);

        }

        else if (!(practiceList.getSelectionModel().isEmpty())) {
            if (selectedItem.contains(" ") || selectedItem.contains("-")) {
                playNameWithMultipleNames(practiceList);

            } else {
                name = practiceList.getSelectionModel().getSelectedItem();
                name = name.trim();
                pathToFile = "Database/" + name + "/Database-Recordings/"
                        + HomeViewController.getNamesHashMap().get(name);
                
                PlayRecordings.handlingPlayingRecordings(pathToFile);
            }
        }
    }

    /**
     * Allows the application play names that have multiple name in them by concatenating multiple different names
     * @param practiceList
     */
    public void playNameWithMultipleNames(ListView practiceList){
        listOfFiles.clear();
        name = practiceList.getSelectionModel().getSelectedItem().toString();
        name = name.replaceAll("-", " ");
        String[] individualNames = name.split(" ");

        for (String str : individualNames) {
            pathToFile = "Database/" + str + "/Database-Recordings/"
                    + HomeViewController.getNamesHashMap().get(str);
            
            listOfFiles.add(new File(pathToFile));
        }
        concatAndPlay(practiceList);

    }

    /**
     * This is a method that concatenates the audio files and then plays it
     * @param practiceList
     */
    public void concatAndPlay(ListView practiceList){
        Service<Void> backgroundThread = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        ConcateAudioFiles.createConcatFile(listOfFiles, "Concat-Recordings/" + produceFileName(practiceList));
                        return null;
                    }

                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                PlayRecordings.handlingPlayingRecordings("Concat-Recordings/" + produceFileName(practiceList) + ".wav");
            }
        });
        backgroundThread.start();

    }

    /**
     * This method creates a name that is suitable for a audio recording file
     * @param practiceList
     * @return
     */
    public String produceFileName(ListView practiceList){
        String name = practiceList.getSelectionModel().getSelectedItem().toString();
        name = name.trim();
        name = name.replaceAll(" ", "_");
        name = name.replaceAll("-", "_");
        return name;
    }


    /**
     * Allows the user create recordings for a selected name and makes sure that the
     * user has selected a recording to practice by changing the scene to Record
     * scene
     *
     * @throws IOException
     */
    @FXML
    public void createRecordings(ListView practiceList) throws IOException {
        listOfFiles.clear();

        if (practiceList.getSelectionModel().isEmpty()) {
            Alerts.show("Please make a selection first",ButtonType.OK,null);
        } else {
            String selectedItem = practiceList.getSelectionModel().getSelectedItem().toString();
            if (selectedItem.contains(" ") || selectedItem.contains("-")) {
                name = practiceList.getSelectionModel().getSelectedItem().toString();
                name = name.replaceAll("-", " ");
                String[] individualNames = name.split(" ");

                for (String str : individualNames) {
                    pathToFile = "Database/" + str + "/Database-Recordings/"
                            + HomeViewController.getNamesHashMap().get(str);
                    listOfFiles.add(new File(pathToFile));
                }
                concatAndRecord(practiceList);

            } else {
                RecordViewController.recordingForUserRecording();
                Main.changeSceneRecord();
            }
        }
    }

    /**
     * This is a method that concatenates the audio files and then records the users version of the name
     * @param practiceList
     */
    public void concatAndRecord(ListView practiceList) {

        Service<Void> backgroundThread = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        produceFileName(practiceList);
                        
                        ConcateAudioFiles.createConcatFile(listOfFiles, "Concat-Recordings/" + produceFileName(practiceList));
                        return null;
                    }

                };
            }
        };

        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                RecordViewController.recordingForUserRecording();
                Main.changeSceneRecord();
            }
        });
        backgroundThread.start();
    }

}
