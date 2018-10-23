package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import helpers.Alerts;
import helpers.BashCommandWorker;
import helpers.MakeHeadingNameFit;
import helpers.SideButtons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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
		String[] individualNames = PracticeMenuController.getSelectedName().split("[-\\s]");

		for ( String ss : individualNames) {
			System.out.println(ss);
		}
		ObservableList<String> individualNamesObList = FXCollections.observableArrayList(individualNames);
		String tempCurrentName = PracticeMenuController.getSelectedName();

		name.setText(MakeHeadingNameFit.changeName(tempCurrentName));
		selectedNames.setItems(individualNamesObList);

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleBadButton() {

		String selectionName = selectedNames.getSelectionModel().getSelectedItem();
		if (selectionName == null){
			Alerts.show("Please select a name to rate",ButtonType.OK,null);

		} else {
			System.out.println(HomeViewController.getNamesHashMap().get(currentName));
			BashCommandWorker creationDirectoryWorker = new BashCommandWorker("badRecordingMessage='" + HomeViewController.getNamesHashMap().get(selectionName) + "'\n" +
					"\n" +
					"if ! grep -qF \"$badRecordingMessage\" BadRecordingList.txt ; then " +
					"echo \"$badRecordingMessage\" >> BadRecordingList.txt ; " +
					"fi\n" +
					"if ! grep -qF \"$badRecordingMessage recording for " + selectionName + " has a bad quality\" ./Database/" + selectionName + "/Ratings.txt ; then " +
					"echo \"$badRecordingMessage recording for " + selectionName + " has a bad quality\" >> ./Database/" + selectionName + "/Ratings.txt ; " +
					"fi");
			Alerts.show("This recording has been rated as bad, in the future we will try to provide you with better recordings",ButtonType.OK,null);

		}

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleGoodButton() {
		String selectionName = selectedNames.getSelectionModel().getSelectedItem();
		if (selectionName == null){
			Alerts.show("Please select a name to rate",ButtonType.OK,null);

		} else {
			BashCommandWorker creationDirectoryWorker = new BashCommandWorker("sed -i '/" + HomeViewController.getNamesHashMap().get(selectionName) + "/d' ./BadRecordingList.txt; \n" +
					"sed -i '/" + HomeViewController.getNamesHashMap().get(selectionName) + " recording for " + selectionName + " has a bad quality/d' ./Database/" + selectionName + "/Ratings.txt ;");

			Alerts.show("This recording has been rated as good. Thank you for your feedback!", ButtonType.OK, null);
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
