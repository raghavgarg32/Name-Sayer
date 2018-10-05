package namesayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.SwingWorker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Calendar;
import java.util.Date;  

public class ConfirmView {

    private SwingWorker<Void,Void> _playWorker;

    @FXML
    private Button delete,save,playUser,redo,playDataBase;

    @FXML
    private Label nameLabel;

    /**
     * handleDeleteButton is call back function that is called when the delete button is pressed,
     * it deletes the temp.wav file and goes back to the practice menu 
     */
    @FXML
    public void handleDeleteButton() {
        String name = PracticeMenuController.getCurrentNameWithoutNumber();
        
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+"/User-Recordings/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            Main.changeScenePractice();
        }
    }

    /**
     * handleSaveButton is a call back function that is called when the save button is pressed, it changes
     * the scene back to the save menu.
     */
    @FXML
    public void handleSaveButton() {
    	String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());
    	
    	System.out.println(timeStamp);
    	
    	String folderName = PracticeMenuController.getCurrentNameWithoutNumber();
    	if(folderName.contains(" ")) {
    		folderName = folderName.replaceAll("\\s","_");
    	}
    	
    	System.out.println(folderName);
    	
        ProcessBuilder saveBuilder = new ProcessBuilder("/bin/bash","-c","mv " + System.getProperty("user.dir") + "/User-Recordings/temp.wav" +  
        		" " + System.getProperty("user.dir") + "/User-Recordings/" + "se206_" + timeStamp + "_" + folderName +".wav");
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
        Main.changeScenePractice();
        
    }
    
    /**
     * handlePlayUserButton is a callback function that is called when the play button is pressed, it plays the current
     * .wav file selected.
     */
    @FXML
    public void handlePlayUserButton() {

    	//Swing worker used for concurrency
        _playWorker = new SwingWorker<Void,Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                String name = PracticeMenuController.getCurrentNameWithoutNumber();
                
                String number = RecordView.getNumberOfRecordings();

                AudioInputStream stream;
                AudioFormat format;
                DataLine.Info info;
                SourceDataLine sourceLine;


                try {
                    stream = AudioSystem.getAudioInputStream(new File("User-Recordings/temp.wav"));
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
        _playWorker.execute();

    }


    /**
     * handleRedoButton is a callback function that is called when the redo button is pressed, it deletes the temp
     * .wav file and changes scene
     */
    @FXML
    public void handleRedoButton() {
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {

            Main.changeSceneRecord();
        }
    }
    
    /**
     * handlePlayDBButton is a callback function that is called when the play button for database recordings is called
     * when the play button for database recordings is called.
     */

    @FXML
    public void handlePlayDBButton() {

    	// swingworker used to add an element of concurrency 
        _playWorker = new SwingWorker<Void,Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                String name = PracticeMenuController.getCurrentNameWithoutNumber();
                
                List<String> databaseList = DataBaseController.getDatabaseList();
                List<String> nameList = DataBaseController.getNamesWithNumbers();

                String path = databaseList.get(nameList.indexOf(name));
                String pathToFile = "Database/"+name+"/Database-Recordings/"+path+".wav";

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

        _playWorker.execute();

    }

    public void setNameLabel(String name){
        nameLabel.setText(name);
    }
    

}