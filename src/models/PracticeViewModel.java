package models;

import controllers.*;
import helpers.Alerts;
import helpers.ConcateAudioFiles;
import helpers.MakeHeadingNameFit;
import helpers.PlayRecordings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
        System.out.println("This is the selected item " + selectedItem);
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
                System.out.println("This is the PATH TO THE FILE " + pathToFile);
                PlayRecordings.handlingPlayingRecordings(pathToFile);
            }
        }
    }


    public void playNameWithMultipleNames(ListView practiceList){
        listOfFiles.clear();
        name = practiceList.getSelectionModel().getSelectedItem().toString();
        name = name.replaceAll("-", " ");
        String[] individualNames = name.split(" ");

        for (String str : individualNames) {
            pathToFile = "Database/" + str + "/Database-Recordings/"
                    + HomeViewController.getNamesHashMap().get(str);
            System.out.println(pathToFile);
            listOfFiles.add(new File(pathToFile));
        }
        concatAndPlay(practiceList);

    }

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
                RecordView.recordingForUserRecording();
                Main.changeSceneRecord();
            }
        }
    }

    public void concatAndRecord(ListView practiceList) {


        Service<Void> backgroundThread = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        produceFileName(practiceList);
                        System.out.println("Concat-Recordings/" + produceFileName(practiceList));
                        ConcateAudioFiles.createConcatFile(listOfFiles, "Concat-Recordings/" + produceFileName(practiceList));
                        return null;
                    }

                };
            }
        };

        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                RecordView.recordingForUserRecording();
                Main.changeSceneRecord();
            }
        });
        backgroundThread.start();
    }

}
