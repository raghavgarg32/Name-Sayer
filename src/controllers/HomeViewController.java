package controllers;

import helpers.Alerts;
import helpers.SideButtons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.HomeViewModel;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Sets up the initial scene for the user which allow the user to add names to playlist which they can practice.
 *
 */
public class HomeViewController extends SideButtons implements Initializable {

	public static List<String> selectionForPractice = new ArrayList<>();

	private static ArrayList<String> namesArrayList = new ArrayList<>();

	private static List<String> databaseList = new ArrayList<>();

	private static String nameToDelete;

	@FXML
	private ListView<String> databaseNameList;

	@FXML
	private ListView<String> playList;

	private static ObservableList<String> namesObservableList;

	@FXML
	public TextField userNameInput;

	private static String currentName;

	private Alert alert;

	private static HashMap<String, String> namesHashMap = new HashMap<>();

	private HomeViewModel homeViewModel;

	private List<String> nameList;

	@FXML
	private Button closeButton;

	public static HashMap<String, String> getNamesHashMap() {
		return namesHashMap;
	}

	/**
	 * This is a callback function thats called when the practice button is called,
	 * it changes the scene when the callback function is called
	 */
	@FXML
	public void handlePracticeBtn() {
		if (!selectionForPractice.isEmpty()) {
			getDatabaseList();
			Main.changeScenePractice();
		} else {
            Alerts.show("Plesase add names to your playlist.",ButtonType.OK,null);
        }
	}

	@FXML
	public void uploadPlayList() {
		homeViewModel.upload(namesObservableList, selectionForPractice,playList);

	}

	/**
	 * The method getBadRecordings will return an ArrayList of type string containing the contents of the file
	 * BadRecordingList.txt
	 * @return
	 */
	public ArrayList<String> getBadRecordings() {
		Scanner inputFromBadRecordingList = null;
		try {
			inputFromBadRecordingList = new Scanner(new File("BadRecordingList.txt"));
		} catch (FileNotFoundException e) {

		}
		ArrayList<String> badlist = new ArrayList<String>();
		while (inputFromBadRecordingList.hasNext()) {
			badlist.add(inputFromBadRecordingList.next());
		}
		inputFromBadRecordingList.close();

		return badlist;
	}

	/**
	 * uploadPlayList is a callback function executed when the button 'Upload playlist' is clicked. This method allows a user to navigate
	 * their file system and upload a .txt file. The contents of the .txt file should be names stored in the database. The method will
	 * populate the listview with the names in the .txt file. If any names don't exist in the database, the method will throw an alert
	 * and the user will be informed of this. However, even though an alert is thrown, any names in the .txt file which exist in the database
	 * will be added to the listview
	 */
	@FXML
	public void addToPlayList() {
		String name = userNameInput.getText().toLowerCase();
		if(name.length() > 50) {
			Alerts.show("Please enter names less than 50 characters long", ButtonType.OK, null);
		}
		else if (name.length() > 0 && name.length() < 50) {

			if(name.lastIndexOf('-') == name.trim().length() - 1) {
				Alerts.show("This name doesn't exist in our database, please add another name.",ButtonType.OK,null);
				return;
			}

			name = name.replace("-", " ");
			//Create an array of all names
			String[] singleNamesArray = name.split(" ");

			Boolean nameExists = true;

			for (String singleName : singleNamesArray) {
				for (String database : databaseList) {
					if (!namesArrayList.contains(singleName)) {

						nameExists = false;
					}
				}
			}
			if (nameExists) {
				playList.getItems().add(userNameInput.getText().toLowerCase());
				selectionForPractice.add(userNameInput.getText().toLowerCase());
			} else {
                Alerts.show("This name doesn't exist in our database, please add another name.",ButtonType.OK,null);
            }
			userNameInput.clear();
		}

	}

	/**
	 * A callback function that is executed when the button 'quit' is clicked, this method will close the namesayer application
	 */
	@FXML
	public void handleQuitButton() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * A callback fucntion that will save the current playlist that the user has
	 */
	@FXML
	public void handleExportPlayListButton() {
		homeViewModel.exportPlayListButton(selectionForPractice);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeViewModel = new HomeViewModel();
		databaseNameList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		namesObservableList = FXCollections.observableArrayList();
		databaseNameList.setItems(namesObservableList);

		//listens for changes in the text field and then searches
		userNameInput.textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				triggerSearch();
			}

		});
	}

	/**
	 * This method starts the search process using the search method when the user starts typing in the text field
	 */
	public void triggerSearch(){
		String name = userNameInput.getText().trim();
		name = name.replace("-", " ");
		

		if (name == null || name.isEmpty() || !name.contains(" ")) {
			search((String) name);
		} else {
			search((String) name.substring(name.lastIndexOf(" "), name.length()).trim());
		}

	}

	/**
	 * The name user selects in the play list list view is set as th name the user might want to delete
	 */
	@FXML
	public void handleClickOnPLayList(){
		nameToDelete = playList.getSelectionModel().getSelectedItem();
	}

	/**
	 * Completes necessary tasks when the user clicks on the list view rows
	 */
	@FXML
	public void handleClickOnCreationList(){
		appendUserSelectedName();
		dealWithDashesInNames();
	}

	/**
	 * Appends the name that the user selects from the list view to the textfield
	 */
	public void appendUserSelectedName() {
		currentName = databaseNameList.getSelectionModel().getSelectedItem();

		//Check if the user has begun search for a new name
		if (userNameInput.getText().isEmpty() || userNameInput.getText() == null || userNameInput.getText().charAt(userNameInput.getText().length() - 1) == ' '
				|| userNameInput.getText().trim().charAt(userNameInput.getText().length() - 1) == '-') {
			userNameInput.setText(userNameInput.getText() + currentName);
		}
	}

	/**
	 * This method allows the program to take in names that have dashes in them and deal with as the program would deal
	 * with spaces
	 */
	public void dealWithDashesInNames(){
		String output;
		List<Integer> indexOfDashes = new ArrayList<Integer>();
		nameList = new ArrayList<String>();


		String name = userNameInput.getText();
		for(int i = 0;i<name.length();i++) {
			if(name.charAt(i) == '-') {
				indexOfDashes.add(i);
			}
		}

		name = name.replace("-", " ");

		String[] namesInTextField = name.trim().split(" ");

		for (int i = 0; i < namesInTextField.length; i++) {
			nameList.add(namesInTextField[i]);
		}

		if (!namesArrayList.contains(nameList.get(nameList.size() - 1))) {
			nameList.remove(nameList.size() - 1);
			nameList.add(databaseNameList.getSelectionModel().getSelectedItem());
		}

		output = String.join(" ", nameList) + " ";
		StringBuilder finalOutput = new StringBuilder(output);

		for(Integer index: indexOfDashes) {
			finalOutput.setCharAt(index.intValue(), '-');
		}

		userNameInput.setText(finalOutput.toString());
		
	}

	/**
	 * A callback function called when the button 'Clear Playlist' is clicked. This method will clear the current items in the listview
	 * playList after receiving confirmation in the form of an alert from the user.
	 */
	@FXML
	public void clearPlayList(){
		if (!selectionForPractice.isEmpty()) {
			alert = Alerts.show("Do you want to clear your playlist", ButtonType.NO, ButtonType.YES);
			if (alert.getResult() == ButtonType.YES) {
				playList.getItems().clear();
				selectionForPractice.clear();
			}
			alert.close();
		} else {
	        Alerts.show("Plesase add names to your playlist.",ButtonType.OK,null);
		}
	}

	/**
	 * A callback function called when the button 'Delete' is clicked. This method will delete the current selected item in the listview
	 * playList after receiving confirmation in the form of an alert from the user.
	 */
	@FXML
	public void deleteSelectedPlayListName(){
		if (playList.getSelectionModel().getSelectedItem() == null) {
			Alerts.show("Please select name in your playlist to delete",ButtonType.OK,null);
		} else {
			alert = Alerts.show("Would you like to delete your selected name?.", ButtonType.NO, ButtonType.YES);
            if (alert.getResult() == ButtonType.YES) {
				playList.getItems().remove(nameToDelete);
				selectionForPractice.remove(nameToDelete);
            }
            alert.close();

		}
	}

	/**
	 * The method search takes in a name to 'search for'. The method will find which name in the database are similar and similar names will
	 * be displayed at the top of the listview
	 * @param nameToSearch
	 */
	@FXML
	public void search(String nameToSearch) {
		List<String> foundList = new ArrayList<String>();
		List<String> notFoundList = new ArrayList<String>();

		if (userNameInput.getText() != null && (nameToSearch.isEmpty())) {
			databaseNameList.setItems(namesObservableList);
		}

		nameToSearch = nameToSearch.toLowerCase();
		ObservableList<String> finalList = FXCollections.observableArrayList();

		for (String name : namesArrayList) {
			boolean match = true;
			if (!(name.contains(nameToSearch))) {
				match = false;
				notFoundList.add(name);
			}
			if (match) {
				foundList.add(name);
			}
		}

		finalList.addAll(foundList);
		finalList.addAll(notFoundList);

		databaseNameList.setItems(finalList);
	}

	/**
	 * This allows the user to add there own recording to the database,
	 */
	public static void addingNewDBRecording(String databaseName, String realName) {
		databaseList.add(databaseName);
		namesArrayList.add(realName);
		namesObservableList.add(realName);
		namesHashMap.put(realName, databaseName);
	}

	/**
	 * Helper method which gets the observable list all of the names the user wants to practice
	 */
	public static ObservableList<String> getNamesForPracticeObservableList() {
		ObservableList<String> namesForPracticeObservableList = FXCollections.observableArrayList();
		for (String name : selectionForPractice) {
			namesForPracticeObservableList.add(name);
			
		}

		return namesForPracticeObservableList;
	}

	/**
	 * Method which returns the names in the database as an array list
	 */
	public static ArrayList<String> getNamesWithoutNumbers() {
		return namesArrayList;
	}

	/**
	 * Method which returns the names in the database as an observable list
	 *
	 * @return
	 */
	public static ObservableList<String> getNamesObservableList() {
		return namesObservableList;
	}

	/**
	 * method which returns the databaselist, all of the database audio file names
	 *
	 * @return
	 */
	public static List<String> getDatabaseList() {

		return databaseList;
	}


	/**
	 * Helper method to read recordings from the database folder and also enables the program to only choose all recordings
	 * that have non bad recordings but if all versions of the name has bad recordings then this method will choose the
	 * last one.
	 */

	public void gettingRecordings() {
		Task<Void> gettingRecordingsWorker = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				ArrayList<String> nameList = new ArrayList<String>();

				try {
					ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c",
							"cd Database;\n" + "\n" + "for i in $(ls); do\n" + "cd $i\n" + "\n"
									+ "cd Database-Recordings\n" + "ls -1 *.wav\n" + "cd ..\n" + "cd ..\n" + "\n"
									+ "done");
					Process process = builder.start();

					InputStream stdout = process.getInputStream();
					BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

					String line = null;

					while ((line = stdoutBuffered.readLine()) != null) {
						databaseList.add(line);
						line = line.substring(0, line.length() - 4);
						line = line.substring(line.lastIndexOf('_') + 1);
						namesArrayList.add(line);
						namesObservableList.add(line);

					}
					stdoutBuffered.close();
					ArrayList<String> namesBadRecordings = new ArrayList<>();
					for (String actualName : getBadRecordings()) {
						actualName = actualName.substring(0, actualName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						namesBadRecordings.add(actualName);
					}
					

					for (String databaseName : databaseList) {
						String actualName = databaseName.substring(0, databaseName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						int occurrences = Collections.frequency(namesArrayList, actualName);

						if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))) {
							if (!getBadRecordings().contains(databaseName)) {
								namesHashMap.put(actualName, databaseName);
							}
						} else {
							namesHashMap.put(actualName, databaseName);

						}
					}
				} catch (IOException ioe) {

				}
				return null;
			}
		};
		new Thread(gettingRecordingsWorker).start();
	}


}