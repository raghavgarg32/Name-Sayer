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

public class HomeViewController extends SideButtons implements Initializable {

	public static List<String> selectionForPractice = new ArrayList<>();

	private static ArrayList<String> nameArrayList = new ArrayList<>();

	private static List<String> databaseList = new ArrayList<>();

	private static String nameToDelete;

	@FXML
	private ListView<String> databaseNameList;

	@FXML
	private ListView<String> playList;

	private static ObservableList<String> allNamesOfDatabaseRecording;

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
		homeViewModel.upload(allNamesOfDatabaseRecording, selectionForPractice,playList);

	}

	public ArrayList<String> getBadRecordings() {
		Scanner inputFromBadRecordingList = null;
		try {
			inputFromBadRecordingList = new Scanner(new File("BadRecordingList.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> badlist = new ArrayList<String>();
		while (inputFromBadRecordingList.hasNext()) {
			badlist.add(inputFromBadRecordingList.next());
		}
		inputFromBadRecordingList.close();

		return badlist;
	}

	@FXML
	public void addToPlayList() {
		String name = userNameInput.getText();
		if (name.length() > 0) {
			name = name.replace("-", " ");
			String[] singleNamesArray = name.split(" ");

			Boolean nameExists = true;

			for (String singleName : singleNamesArray) {
				for (String database : databaseList) {
					if (!nameArrayList.contains(singleName)) {

						nameExists = false;
					}
				}
			}
			if (nameExists) {
				playList.getItems().add(userNameInput.getText());
				selectionForPractice.add(userNameInput.getText());
			} else {
                Alerts.show("This name doesn't exist in our database, please add another name.",ButtonType.OK,null);
            }
			userNameInput.clear();
		}

	}

	@FXML
	public void handleQuitButton() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void handleExportPlayListButton() {
		homeViewModel.ExportPlayListButton(selectionForPractice);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeViewModel = new HomeViewModel();
		databaseNameList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// This is just test data for the allNamesOfDatabaseRecording
		allNamesOfDatabaseRecording = FXCollections.observableArrayList();
		databaseNameList.setItems(allNamesOfDatabaseRecording);


		userNameInput.textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				searchUserName();
			}

		});
	}

	public void searchUserName(){
		String name = userNameInput.getText().trim();
		name = name.replace("-", " ");
		System.out.println(name);

		if (name == null || name.isEmpty() || !name.contains(" ")) {
			search((String) name);
		} else {
			search((String) name.substring(name.lastIndexOf(" "), name.length()).trim());
			System.out.println(
					"We're searching for " + name.substring(name.lastIndexOf(" "), name.length()).trim());
		}

	}

	@FXML
	public void handleClickOnPLayList(){
		nameToDelete = playList.getSelectionModel().getSelectedItem();

	}

	@FXML
	public void handleClickOnCreationList(){
		appendUserSelectedName();
		dealWithDashesInNames();
	}

	public void appendUserSelectedName() {
		currentName = databaseNameList.getSelectionModel().getSelectedItem();

		if (userNameInput.getText().isEmpty() || userNameInput.getText() == null || userNameInput.getText().charAt(userNameInput.getText().length() - 1) == ' '
				|| userNameInput.getText().trim().charAt(userNameInput.getText().length() - 1) == '-') {
			userNameInput.setText(userNameInput.getText() + currentName);
		}
	}

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

		if (!nameArrayList.contains(nameList.get(nameList.size() - 1))) {
			nameList.remove(nameList.size() - 1);
			nameList.add(databaseNameList.getSelectionModel().getSelectedItem());
		}

		output = String.join(" ", nameList) + " ";
		StringBuilder finalOutput = new StringBuilder(output);

		for(Integer index: indexOfDashes) {
			finalOutput.setCharAt(index.intValue(), '-');
		}

		userNameInput.setText(finalOutput.toString());
		System.out.println("This is the name of the customer " + currentName);
	}

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

	@FXML
	public void deleteSelectedPlayListName(){
		if (playList.getSelectionModel().getSelectedItem().equals(null)) {
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

	@FXML
	public void search(String nameToSearch) {
		List<String> foundList = new ArrayList<String>();
		List<String> notFoundList = new ArrayList<String>();

		if (userNameInput.getText() != null && (nameToSearch.isEmpty())) {
			databaseNameList.setItems(allNamesOfDatabaseRecording);
		}

		nameToSearch = nameToSearch.toLowerCase();
		ObservableList<String> finalList = FXCollections.observableArrayList();

		for (String name : nameArrayList) {
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


	public static void addingNewDBRecording(String databaseName, String realName) {
		databaseList.add(databaseName);
		nameArrayList.add(realName);
		allNamesOfDatabaseRecording.add(realName);
		namesHashMap.put(realName, databaseName);
	}

	/**
	 * Helper method which gets the items from the selectionForPractice
	 */
	public static ObservableList<String> getNamesForPracticeObservableList() {
		ObservableList<String> namesForPracticeObservableList = FXCollections.observableArrayList();
		for (String name : selectionForPractice) {
//			System.out.println("This is the name " + name);
//			String[] individualNames = name.split(" ");
//			if (name.contains(" ")) {
//				List<File> listOfFiles = new ArrayList<>();
//				for (String individualName : individualNames) {
//					System.out.println("Individual names " + individualName);
//					System.out.println("Database names " + namesHashMap.get(individualName));
//
//					File file = new File("./Database/" + individualName + "/Database-Recordings/" + namesHashMap.get(individualName));
//
//					listOfFiles.add(file);
//
//				}
//
//			}
			namesForPracticeObservableList.add(name);
			System.out.println("Items " + namesForPracticeObservableList);
		}

		return namesForPracticeObservableList;
	}

	/**
	 * Method which returns the names in the database without numbers
	 */
	public static ArrayList<String> getNamesWithoutNumbers() {
		return nameArrayList;
	}

	/**
	 * Method which returns the names in the database but with numbers
	 *
	 * @return
	 */
	public static ObservableList<String> getAllNamesOfDatabaseRecording() {
		return allNamesOfDatabaseRecording;
	}

	/**
	 * method which returns the databaselist
	 *
	 * @return
	 */

	public static List<String> getDatabaseList() {

		return databaseList;
	}


	/**
	 * Helper method to read recordings from the database folder
	 */
	public void gettingRecordings() {
		// This swingworker gets all of the creations from the NameSayer directory
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
						nameArrayList.add(line);
						allNamesOfDatabaseRecording.add(line);

					}
					stdoutBuffered.close();
					ArrayList<String> namesBadRecordings = new ArrayList<>();
					for (String actualName : getBadRecordings()) {
						actualName = actualName.substring(0, actualName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						namesBadRecordings.add(actualName);
					}
					System.out.println(namesBadRecordings);

					for (String databaseName : databaseList) {
						String actualName = databaseName.substring(0, databaseName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						int occurrences = Collections.frequency(nameArrayList, actualName);

						if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))) {
							if (!getBadRecordings().contains(databaseName)) {
								namesHashMap.put(actualName, databaseName);
							}
						} else {
							namesHashMap.put(actualName, databaseName);

						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				return null;
			}
		};
		new Thread(gettingRecordingsWorker).start();
	}


}