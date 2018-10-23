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

/**
 * This is the model for the different confirm record controllers, they both use code from this class
 */
public class ConfirmRecordingsModel {
    @FXML
    private Label nameLabel;

    @FXML
    private TextField loopNumber;

    private String currentName;

    private String pathToFile;

    private String currentNameAsAudioName;

    private String timeStamp;


    /**
     * This method deletes the users recording if the decide to delete it
     * @param fileLocation
     */
    public void deleteRecordings(String fileLocation){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/"+fileLocation+"/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
    }

    /**
     * This method saves users recordings which they recorded to practice if the decide to save it. This is done using the
     * saveRecording method
     * */
    public void saveUserRecordings(){
        handleRewards();
        settingPartsOfNameForAudio(PracticeViewController.getCurrentName(false));

        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());

        ProcessBuilder saveBuilder = new ProcessBuilder("/bin/bash", "-c",
                "mv " + System.getProperty("user.dir") + "/User-Recordings/temp.wav" + " "
                        + System.getProperty("user.dir") + "/User-Recordings/" + "se206_" + timeStamp + "_" + currentNameAsAudioName
                        + ".wav");


        saveRecordings(saveBuilder);

    }

    /**
     * This method saves users recordings which they recorded to put in the database if the decide to save it.This is done using the
     * saveRecording method
     * */
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

    /**
     * This method sets up the different parts of the name under which they will save the new recording
     * @param currentNameToSave
     */
    public void settingPartsOfNameForAudio(String currentNameToSave){
        currentNameAsAudioName = currentNameToSave;
        if (currentNameAsAudioName.contains(" ")) {
            currentNameAsAudioName = currentNameAsAudioName.replaceAll("\\s", "_");
        }

        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());

    }


    /**
     * This method saves the recording no matter what the user's intentions are
     * @param processBuilder
     */
    public void saveRecordings(ProcessBuilder processBuilder){

        

        

        ProcessBuilder saveBuilder = processBuilder;
        try {
            Process saveProcess = saveBuilder.start();
            saveProcess.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block

        }
    }

    /**
     * This method handles the rewards after the user saves their recordings and provides them appropiate alert
     */
    public void handleRewards(){
        FXMLLoader rewardLoader = new FXMLLoader();
        rewardLoader.setLocation(getClass().getResource("/views/RewardMenu.fxml"));
        try {
            rewardLoader.load();
        } catch (IOException e1) {
            // TODO Auto-generated catch block

        }
        RewardMenuController rewardController = rewardLoader.getController();
        
        RewardMenuController.getRewardPoint();
        

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

        String name = PracticeViewController.getCurrentName(false).trim();

        if (name.contains(" ")|| name.contains("-")) {
            
            name = name.replace(" ", "_");
            name = name.replace("-", "_");

            pathToFile = "Concat-Recordings/" + name + ".wav";
            
            PlayRecordings.handlingPlayingRecordings(pathToFile);
        } else {

            List<String> databaseList = HomeViewController.getDatabaseList();
            List<String> nameList = HomeViewController.getNamesObservableList();

            String path = databaseList.get(nameList.indexOf(name));
            pathToFile = "Database/" + name + "/Database-Recordings/" + path;
            
            PlayRecordings.handlingPlayingRecordings(pathToFile);

        }

    }

    /**
     * This sets the heading of the scene as the user's selected name
     * @param name
     */
    public void setCurrentName(String name){
        currentName = name;
    }

    /*
     * This sets the number of time the user wants to loop through their recording and the database recording together
     */
    public void setLoopNumber(TextField loopNumberTextField){
        loopNumber = loopNumberTextField;

    }

    /**
     * Callback function for the 'Compare' button, this will play the audio of the user recording and the corresponding database recording as
     * many times as the user specficies.
     */
    @FXML
    public void compareUserRecordingsWithDB() {
        boolean isConcatFile = false;
        String name = PracticeViewController.getCurrentName(false).trim();

        if(name.contains(" ") || name.contains("-")) {
            isConcatFile = true;
        }
        else {
            isConcatFile = false;
        }

        name = name.replace(" ", "_");
        name = name.replace("-", "_");

        try{
            
            //      String concatNewNameFile = currentName.trim().replace(" ", "_");
            
            //      concatPathToFile = "Concat-Recordings/" + concatNewNameFile + ".wav";

            if(isConcatFile) {
                pathToFile = "Concat-Recordings/" + name + ".wav";
                
            }
            else if(!isConcatFile) {
                List<String> databaseList = HomeViewController.getDatabaseList();
                List<String> nameList = HomeViewController.getNamesObservableList();
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
