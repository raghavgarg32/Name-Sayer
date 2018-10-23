package controllers;

import helpers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import models.PracticeViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PracticeMenuController extends SideButtons implements Initializable {

	private ObservableList<String> selectedNamesToPractice;

	private static String currentName;

	@FXML
	public Label names;

	@FXML
	public ListView<String> practiceList; // List of practice names that the user selected

	@FXML
	public ListView<String> userRecordingCreations; // List of user attempt at recording themeselves saying the name

	private PracticeViewModel practiceViewModel;

	/**
	 * Callback function which is called when the play button is pressed. It checks
	 * which list is selected and plays the corresponding item
	 *
	 * @throws IOException
	 */
	@FXML
	public void handlePlayDBRecordingButton() throws IOException {
		practiceViewModel.playDBRecording(practiceList);
	}

	@FXML
	public void handlePlayUserRecordingButton() throws IOException {
		// Check which if neither list has been selected
		if (userRecordingCreations.getSelectionModel().isEmpty()) {
			Alerts.show("Please make a selection to play",ButtonType.OK,null);
		}
		// Check which list has been selected
		else if (!(userRecordingCreations.getSelectionModel().isEmpty())) {
			String name = userRecordingCreations.getSelectionModel().getSelectedItem();
			String pathToFile = "User-Recordings/" + name;
			PlayRecordings.handlingPlayingRecordings(pathToFile);
		}

		/**
		 * When the user has selected a database recording or a user recording this will
		 * allow the user to play the recording
		 */

		else if (userRecordingCreations.getSelectionModel().isEmpty() && !(practiceList.getSelectionModel().isEmpty())) {
			String name = practiceList.getSelectionModel().getSelectedItem().replaceAll(" ", "_");

			String pathToFile = "Concat-Recordings/" + name + ".wav";

			PlayRecordings.handlingPlayingRecordings(pathToFile);
		}
	}

	/**
	 * Allows the user create recordings for a selected name and makes sure that the
	 * user has selected a recording to practice by changing the scene to Record
	 * scene
	 *
	 * @throws IOException
	 */
	@FXML
	public void handleCreateButton() throws IOException {
		practiceViewModel.createRecordings(practiceList);
	}


	/**
	 * Allows the user rate user recordings for a selected name and makes sure that
	 * the user has selected a recording to practice by changing the scene to Rate
	 * scene
	 *
	 * @throws IOException
	 */
	@FXML
	public void handleRateButton() throws IOException {
		if (practiceList.getSelectionModel().isEmpty()) {
			Alerts.show("Please select a database recording to review",ButtonType.OK,null);

		} else {
			Main.changeSceneRateMenu();
		}

	}

	/**
	 * Setting up different array lists for the different list views to use
	 *
	 * @param selectedNames
	 *
	 */

	public void names(ObservableList<String> selectedNames) {

		selectedNamesToPractice = selectedNames;

		practiceList.setItems(selectedNamesToPractice);
	}



	public void settingUserListView(String currentName) {
		String tempName = getCurrentName(false);

		ObservableList<String> items = FXCollections.observableArrayList();
		File folder = new File(System.getProperty("user.dir") + "/User-Recordings");
		if (tempName != null) {
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String currentFileName = listOfFiles[i].getName().replaceAll("_", " ");
					System.out.println("This is the current names FILE " + currentFileName);
					if (currentFileName.length() >= 26) {
						System.out.println("This is the current names FILE SOMETHINGS "
								+ currentFileName.substring(26, currentFileName.length() - 4));

						if (currentFileName.substring(26, currentFileName.length() - 4).equals(currentName)) {
							items.add(listOfFiles[i].getName());
						}
					}
				}
			}

			userRecordingCreations.setItems(items);
		}

	}

	/**
	 * Allows user to click on different names and change name label and user
	 * recording array list accordingly
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		practiceViewModel = new PracticeViewModel();
		practiceList.getSelectionModel().select(0);
		currentName = "Name";
		practiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			// Changes the practice view depending on which was the last selected list
			@Override
			public void handle(MouseEvent event) {
				System.out.println(practiceList.getSelectionModel().getSelectedItem());
				currentName = practiceList.getSelectionModel().getSelectedItem();
				String tempCurrentName = currentName;
				settingUserListView(currentName);
				names.setText(MakeHeadingNameFit.changeName(tempCurrentName));
			}
		});
	}

	public static String getSelectedName() {
		return currentName;
	}


	/**
	 * getter method to get the current name but with the number removed
	 *
	 * @return
	 */
	public static String getCurrentName(Boolean addDBRecording) {
		if (addDBRecording) {
			return AddDBRecordingsViewController.getCurrentDBName();
		}
		return currentName;
	}

}