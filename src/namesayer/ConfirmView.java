package namesayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ConfirmView {
	
	@FXML
	private Button delete,save,playUser,redo,playDataBase;
	
	@FXML
	private Label name;

	@FXML
	public void handleDeleteButton() {
		String name = PracticeMenuController.getCurrentName();
		String number = RecordView.getNumberOfRecordings();
		try {
			Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+
					"/Database/" + name + "/User-Recordings/" + name + number + ".wav"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Main.changeScenePractice();
		}
	}

	@FXML
	public void handleSaveButton() {
		Main.changeScenePractice();
	}

	@FXML
	public void handlePlayUserButton() {
		String name = PracticeMenuController.getCurrentName();
		String number = RecordView.getNumberOfRecordings();
		
		
		AudioInputStream stream;
        AudioFormat format;
        DataLine.Info info;
        SourceDataLine sourceLine;
        
		
		try {
            stream = AudioSystem.getAudioInputStream(new File("Database/"+name+"/User-Recordings/"+name+
    				number+".wav"));
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

	}
	


	@FXML
	public void handleRedoButton() {
		String name = PracticeMenuController.getCurrentName();
		String number = RecordView.getNumberOfRecordings();
		try {
			Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+
					"/Database/" + name + "/User-Recordings/" + name + number + ".wav"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Main.changeSceneRecord();
		}
	}

	@FXML
	public void handlePlayDBButton() {
		String name = PracticeMenuController.getCurrentName();
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

	}
	

}
