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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import javax.swing.*;

/**
 * Sets up the rating system for the database recordings
 */
public class RateViewController extends SideButtons implements Initializable {
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

			System.out.println(DataBaseController.getNamesHashMap().get(currentName));
			BashCommandWorker creationDirectoryWorker = new BashCommandWorker("badRecordingMessage='"+DataBaseController.getNamesHashMap().get(currentName)+"'\n" +
					"\n" +
					"if ! grep -qF \"$badRecordingMessage\" BadRecordingList.txt ; then " +
					"echo \"$badRecordingMessage\" >> BadRecordingList.txt ; " +
					"fi\n" +
					"if ! grep -qF \"$badRecordingMessage recording for "+ selectionName+ " has a bad quality\" ./Database/"+selectionName+"/Ratings.txt ; then " +
					"echo \"$badRecordingMessage recording for "+ selectionName+ " has a bad quality\" >> ./Database/"+selectionName+"/Ratings.txt ; " +
					"fi");

		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"This recording has been rated as bad, in the future we will try to provide you with better recordings", ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			alert.close();
		}

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleGoodButton() {
		String selectionName = currentName;

			BashCommandWorker creationDirectoryWorker = new BashCommandWorker("sed -i '/"+DataBaseController.getNamesHashMap().get(currentName)+"/d' ./BadRecordingList.txt; \n" +
					"sed -i '/"+DataBaseController.getNamesHashMap().get(currentName)+" recording for "+ selectionName+ " has a bad quality/d' ./Database/"+selectionName+"/Ratings.txt ;");

		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"This recording has been rated as good. Thank you for your feedback!", ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			alert.close();
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
