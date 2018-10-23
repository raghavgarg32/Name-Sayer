package models;

import controllers.*;
import helpers.Alerts;
import helpers.PlayRecordings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConfirmRecordingsModel {
    @FXML
    private Label nameLabel;

    @FXML
    private TextField loopNumber;

    private String name;

    private String currentName;

    private String pathToFile;

    private String concatPathToFile;

    private String currentNameAsAudioName;

    private String timeStamp;



    public void deleteRecordings(String fileLocation){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/"+fileLocation+"/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveUserRecordings(){
        handleRewards();
        settingPartsOfNameForAudio(PracticeMenuController.getCurrentName(false));

        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());

        ProcessBuilder saveBuilder = new ProcessBuilder("/bin/bash", "-c",
                "mv " + System.getProperty("user.dir") + "/User-Recordings/temp.wav" + " "
                        + System.getProperty("user.dir") + "/User-Recordings/" + "se206_" + timeStamp + "_" + currentNameAsAudioName
                        + ".wav");


        saveRecordings(saveBuilder);

    }

    public void saveDBRecordings() {
        settingPartsOfNameForAudio(AddDBRecordingsViewController.getCurrentDBName().toLowerCase());

        ProcessBuilder saveBuilder = new ProcessBuilder("/bin/bash", "-c", "cd Database;\n" +
                "mkdir " + AddDBRecordingsViewController.getCurrentDBName() + ";\n" +
                "mkdir ./" + AddDBRecordingsViewController.getCurrentDBName() + "/Database-Recordings;\n" +
                "mv temp.wav" +
                " ./" + AddDBRecordingsViewController.getCurrentDBName() + "/Database-Recordings/" + "se206_" + timeStamp + "_" + currentNameAsAudioName + ".wav");

        saveRecordings(saveBuilder);
        HomeViewController.addingNewDBRecording("se206_" + timeStamp + "_" + currentNameAsAudioName + ".wav", currentNameAsAudioName);

    }

    public void settingPartsOfNameForAudio(String currentNameToSave){
        currentNameAsAudioName = currentNameToSave;
        if (currentNameAsAudioName.contains(" ")) {
            currentNameAsAudioName = currentNameAsAudioName.replaceAll("\\s", "_");
        }

        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());

    }



    public void saveRecordings(ProcessBuilder processBuilder){

        System.out.println(timeStamp);

        System.out.println(currentNameAsAudioName);

        ProcessBuilder saveBuilder = processBuilder;
        try {
            Process saveProcess = saveBuilder.start();
            saveProcess.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void handleRewards(){
        FXMLLoader rewardLoader = new FXMLLoader();
        rewardLoader.setLocation(getClass().getResource("/views/RewardMenu.fxml"));
        try {
            rewardLoader.load();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        RewardMenuController rewardController = rewardLoader.getController();
        System.out.println("This is reward " + rewardController.getPoints());
        RewardMenuController.getRewardPoint();
        System.out.println("This is reward " + rewardController.getPoints());

        if (rewardController.getPoints() == 10 || rewardController.getPoints() == 20
                || rewardController.getPoints() == 30) {
            Alerts.show("New reward -check it in Rewards", ButtonType.OK,null);

        }
    }

    /**
     * handlePlayButton is a callback function that is called when the play
     * button is pressed, it plays the current .wav file selected.
     */
    @FXML
    public void playUserRecording(String fileLocation) {
        PlayRecordings.handlingPlayingRecordings(fileLocation+"/temp.wav");

    }

    /**
     * handleRedoButton is a callback function that is called when the redo button
     * is pressed, it deletes the temp .wav file and changes scene
     */
    @FXML
    public void redoUserRecording(String fileLocation) {
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/"+fileLocation+"/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            Main.changeSceneRecord();
        }
    }

    /**
     * handlePlayDBButton is a callback function that is called when the play button
     * for database recordings is called when the play button for database
     * recordings is called.
     */
    @FXML
    public void playDBRecording() {

        String name = PracticeMenuController.getCurrentName(false).trim();

        if (name.contains(" ")|| name.contains("-")) {
            System.out.println("this si sitehgwefh rsejgyft rejfd " + name + "asdada" );
            name = name.replace(" ", "_");
            name = name.replace("-", "_");

            pathToFile = "Concat-Recordings/" + name + ".wav";
            System.out.println(pathToFile);
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        } else {

            List<String> databaseList = HomeViewController.getDatabaseList();
            List<String> nameList = HomeViewController.getAllNamesOfDatabaseRecording();

            String path = databaseList.get(nameList.indexOf(name));
            pathToFile = "Database/" + name + "/Database-Recordings/" + path;
            System.out.println(pathToFile);
            PlayRecordings.handlingPlayingRecordings(pathToFile);

        }

    }

    public void setCurrentName(String name){
        currentName = name;
    }

    public void setLoopNumber(TextField loopNumberTextField){
        loopNumber = loopNumberTextField;

    }


    @FXML
    public void compareUserRecordingsWithDB() {
        boolean isConcatFile = false;
        String name = PracticeMenuController.getCurrentName(false).trim();

        if(name.contains(" ") || name.contains("-")) {
            isConcatFile = true;
        }
        else {
            isConcatFile = false;
        }

        name = name.replace(" ", "_");
        name = name.replace("-", "_");

        try{
            System.out.println("this is the name " + currentName + "This is the end");
            //      String concatNewNameFile = currentName.trim().replace(" ", "_");
            System.out.println("Loops " + Integer.parseInt(loopNumber.getText()));
            //      concatPathToFile = "Concat-Recordings/" + concatNewNameFile + ".wav";

            if(isConcatFile) {
                pathToFile = "Concat-Recordings/" + name + ".wav";
                System.out.println("path to file is " + pathToFile);
            }
            else if(!isConcatFile) {
                List<String> databaseList = HomeViewController.getDatabaseList();
                List<String> nameList = HomeViewController.getAllNamesOfDatabaseRecording();
                String path = databaseList.get(nameList.indexOf(name));
                pathToFile = "Database/" + name + "/Database-Recordings/" + path;
            }


            ArrayList<String> namesToPlay = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(loopNumber.getText()); i++) {
                namesToPlay.add(pathToFile);
                namesToPlay.add("User-Recordings/temp.wav");
            }
            String[] namesToPlayArray = namesToPlay.toArray(new String[namesToPlay.size()]);
            PlayRecordings.handlingPlayingRecordings(namesToPlayArray);
        }catch (NumberFormatException ex) {
            Alerts.show("Please enter in a valid integer",ButtonType.OK,null);
        }

    }
}
