package namesayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.LineEvent.Type;
import javax.swing.SwingWorker;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConfirmView extends SideButtons implements Initializable {

	private SwingWorker<Void, Void> _playWorker;

	@FXML
	private Button delete, save, playUser, redo, playDataBase;

	@FXML
	private Thread compareRecordingThread;

	@FXML
	private Label nameLabel;

	@FXML
	private TextField loopNumber;

	private String pathToDBRecording;

	private String name;

	/**
	 * handleDeleteButton is call back function that is called when the delete
	 * button is pressed, it deletes the temp.wav file and goes back to the practice
	 * menu
	 */
	@FXML
	public void handleDeleteButton() {
		String name = PracticeMenuController.getCurrentNameWithoutNumber();

		try {
			Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Main.changeScenePractice();
		}
	}

	/**
	 * handleSaveButton is a call back function that is called when the save button
	 * is pressed, it changes the scene back to the save menu.
	 */
	@FXML
	public void handleSaveButton() {
		FXMLLoader rewardLoader = new FXMLLoader();
		rewardLoader.setLocation(getClass().getResource("RewardMenu.fxml"));
		try {
			rewardLoader.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RewardMenuController rewardController = rewardLoader.getController();
		System.out.println("This is reward " + rewardController.getPoints());
		RewardMenuController.increaseRewardPoint();
		System.out.println("This is reward " + rewardController.getPoints());

		if (rewardController.getPoints() == 10 || rewardController.getPoints() == 20
				|| rewardController.getPoints() == 30) {
			Alert alert = new Alert(Alert.AlertType.NONE, "New reward -check it in Rewards", ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}

		String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Calendar.getInstance().getTime());

		System.out.println(timeStamp);

		String folderName = PracticeMenuController.getCurrentNameWithoutNumber();
		if (folderName.contains(" ")) {
			folderName = folderName.replaceAll("\\s", "_");
		}

		System.out.println(folderName);

		ProcessBuilder saveBuilder = new ProcessBuilder("/bin/bash", "-c",
				"mv " + System.getProperty("user.dir") + "/User-Recordings/temp.wav" + " "
						+ System.getProperty("user.dir") + "/User-Recordings/" + "se206_" + timeStamp + "_" + folderName
						+ ".wav");
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
	 * handlePlayUserButton is a callback function that is called when the play
	 * button is pressed, it plays the current .wav file selected.
	 */
	@FXML
	public void handlePlayUserButton() {

		// Swing worker used for concurrency
		_playWorker = new SwingWorker<Void, Void>() {

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
	 * handleRedoButton is a callback function that is called when the redo button
	 * is pressed, it deletes the temp .wav file and changes scene
	 */
	@FXML
	public void handleRedoButton() {
		try {
			Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/User-Recordings/temp.wav"));
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
	public void handlePlayDBButton() {

		if (PracticeMenuController.getCurrentName().contains(" ")
				|| PracticeMenuController.getCurrentName().contains("-")) {
			// swingworker used to add an element of concurrency
			String name = PracticeMenuController.getCurrentName().replace(" ", "_");
			String pathToFile = "Concat-Recordings/" + name + ".wav";
			PracticeMenuController.handlingPlayingRecordings(pathToFile);
		}

		else {
			// swingworker used to add an element of concurrency
			name = PracticeMenuController.getSelectedName();

			List<String> databaseList = DataBaseController.getDatabaseList();
			List<String> nameList = DataBaseController.getNames();

			String path = databaseList.get(nameList.indexOf(name));
			String pathToFile = "Database/" + name + "/Database-Recordings/" + path;
			PracticeMenuController.handlingPlayingRecordings(pathToFile);

		}

	}

	public void setNameLabel(String name) {
		if (name.length() > 20) {
			name = name.substring(0, 17);
			name = name + "...";
		}
		nameLabel.setText(name);

	}

	// COPY PASTE FROM HERE

	@FXML
	public void handleCompareButton() {

		List<String> databaseList = DataBaseController.getDatabaseList();
		List<String> nameList = DataBaseController.getNames();
		List<File> concatList = new ArrayList<File>();
		name = PracticeMenuController.getCurrentName();

		concatList.add(new File("User-Recordings/temp.wav"));

		if (name.contains(" ") || name.contains("-")) {
			name = name.replace(" ", "_");
			name = name.replace("-", "_");
			concatList.add(new File("Concat-Recordings/" + name + ".wav"));
		} else {
			name = PracticeMenuController.getSelectedName();
			String path = databaseList.get(nameList.indexOf(name));
			pathToDBRecording = path;
			concatList.add(new File("Database/" + name + "/Database-Recordings/" + path));
		}

		System.out.println(concatList);
		

		try {

			int numberToLoop = Integer.parseInt(this.loopNumber.getText());
			System.out.println("Number of times to loop is " + numberToLoop);

			Task<Void> playTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					File concatFile = DataBaseController.createConcatFile(concatList, "Concat-Recordings/tempConcatFile");
					
					for (int i = 0; i < numberToLoop; i++) {
						playCompareRecording("User-Recordings/temp.wav", "Concat-Recordings/tempConcatFile");
					}
					return null;
				}

			};

			compareRecordingThread = new Thread(playTask);
			compareRecordingThread.start();

		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Value in text field should be a number",
					ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				return;
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loopNumber.setText("1");
	}

	public void playCompareRecording(String pathToUserRecording, String pathToDataBaseRecording) {
		
	
		
		
		
		AudioInputStream stream;
		AudioFormat format;
		DataLine.Info info;
		SourceDataLine sourceLine;

		try {
			stream = AudioSystem.getAudioInputStream(new File("Concat-Recordings/tempConcatFile.wav"));
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
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
//		try {
//			stream = AudioSystem.getAudioInputStream(new File(pathToDataBaseRecording+".wav"));
//			format = stream.getFormat();
//
//			info = new DataLine.Info(SourceDataLine.class, format);
//			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
//			sourceLine.open(format);
//
//			sourceLine.start();
//
//			int nBytesRead = 0;
//			int BUFFER_SIZE = 128000;
//			byte[] abData = new byte[BUFFER_SIZE];
//			while (nBytesRead != -1) {
//				try {
//					nBytesRead = stream.read(abData, 0, abData.length);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if (nBytesRead >= 0) {
//					@SuppressWarnings("unused")
//					int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
//				}
//			}
//
//			sourceLine.drain();
//			sourceLine.close();
//		}
//		catch(Exception e) {	
//			e.printStackTrace();
//		}			
	
	}

	public void stopPlayingCompareRecording() {
		compareRecordingThread.interrupt();
		BashCommandWorker stopBuilder = new BashCommandWorker("killall ffmpeg");
	}

}
