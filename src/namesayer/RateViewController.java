package namesayer;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

/**
 * Sets up the rating system for the database recordings
 */
public class RateViewController implements Initializable {
	@FXML
	public Label name;

	@FXML
	public ListView<String> selectedNames;

	private String currentName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		name.setText("Name");

		selectedNames.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//Changes the practice view depending on which was the last selected list
			@Override
			public void handle(MouseEvent event) {
				System.out.println(selectedNames.getSelectionModel().getSelectedItem());
				currentName = selectedNames.getSelectionModel().getSelectedItem();
			}
		});

	}

	public void setlabel(){
		System.out.println("Setting name " + PracticeMenuController.getSelectedName());
		name.setText(PracticeMenuController.getSelectedName());
		String[] individualNames = PracticeMenuController.getSelectedName().split(" ");

		for ( String ss : individualNames) {
			System.out.println(ss);
		}
		ObservableList<String> individualNamesObList = FXCollections.observableArrayList(individualNames);
		String tempCurrentName = PracticeMenuController.getSelectedName();
		if (tempCurrentName.length() > 20){
			tempCurrentName = tempCurrentName.substring(0,17);
			tempCurrentName = tempCurrentName + "...";
		}
		name.setText(tempCurrentName);
		selectedNames.setItems(individualNamesObList);

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleBadButton() {

		String selectionName = currentName;
		String path = "./Database/"+selectionName+"/Ratings/userReview.txt";
		PrintWriter writer;
		try {
			// Create a text file and write to it
			writer = new PrintWriter(path, "UTF-8");
			writer.println("The recording is of bad quality");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			SwingWorker creationDirectoryWorker = new BashCommandWorker("badRecordingMessage='"+currentName+" has a bad recording'\n" +
					"\n" +
					"if ! grep -qF \"$badRecordingMessage\" BadRecordingList.txt ; then " +
					"echo \"$badRecordingMessage\" >> BadRecordingList.txt ; " +
					"fi");
		}
		Main.changeScenePractice();

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleGoodButton() {
		String selectionName = currentName;
		String path = "./Database/"+selectionName+"/Ratings/userReview.txt";
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("The recording is of good quality");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			SwingWorker creationDirectoryWorker = new BashCommandWorker("sed -i '/"+currentName+" has a bad recording/d' ./BadRecordingList.txt ");
			Main.changeScenePractice();
		}
	}

	/**
	 * callback function for the back button wihch changes the scene
	 */
	@FXML
	public void handleBackButton() {
		Main.changeScenePractice();
	}


}
