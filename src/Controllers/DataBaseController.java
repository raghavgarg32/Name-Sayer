package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class DataBaseController extends SideButtons implements Initializable {

	public static List<String> _practiceSelection = new ArrayList<>();

	private static ArrayList<String> nameArrayList = new ArrayList<>();

	private static List<String> databaseList = new ArrayList<>();

	private static String nameToDelete;

	@FXML
	private ListView<String> _creationList;

	@FXML
	private ListView<String> playList;

	private static ObservableList<String> list;

	@FXML
	public TextField userName;

	private List<String> databaseNames;

	@FXML
	private Button practiceBtn;

	private static String currentName;

	private static HashMap<String, String> Names = new HashMap<>();

	private static HashMap<String, String> actualNames = new HashMap<>();

	ArrayList<String> actualNamesBadRecordings = new ArrayList<>();

	private List<String> nameList;

	@FXML
	private Button closeButton;

	public static HashMap<String, String> getNamesHashMap() {
		return Names;
	}

	@FXML
	private ImageView homeImage;

	/**
	 * This is a callback function thats called when the practice button is called,
	 * it changes the scene when the callback function is called
	 */
	@FXML
	public void handlePracticeBtn() {
		if (!_practiceSelection.isEmpty()) {
			getDatabaseList();

			Main.changeScenePractice();
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plesase add names to your playlist.", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}
	}

	/**
	 * uploadPlayList is a callback function executed when the button 'Upload playlist' is clicked. This method allows a user to navigate 
	 * their file system and upload a .txt file. The contents of the .txt file should be names stored in the database. The method will
	 * populate the listview with the names in the .txt file. If any names don't exist in the database, the method will throw an alert
	 * and the user will be informed of this. However, even though an alert is thrown, any names in the .txt file which exist in the database
	 * will be added to the listview
	 */
	@FXML
	public void uploadPlayList() {
		List<String> fileNames = new ArrayList<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			System.out.println(selectedFile.getAbsolutePath());
			try {
				File file = new File(selectedFile.getAbsolutePath());
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					fileNames.add(line.toLowerCase());
				}
				fileReader.close();
				if (!list.containsAll(fileNames)) {

					Alert alert = new Alert(Alert.AlertType.INFORMATION,
							"This text file contains names that aren't in the database, those names will not be added"
									+ "to review",
							ButtonType.OK);
					alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

					alert.showAndWait();
					if (alert.getResult() == ButtonType.OK) {
						alert.close();
					}
				}
				for (String name : fileNames) {
					String[] individualNames = name.split(" ");
					ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(individualNames));
					if (list.containsAll(arrayList)) {
						playList.getItems().add(name);
						_practiceSelection.add(name);

					}
				}

				System.out.println("Contents of file:");
				System.out.println(stringBuffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The method getBadRecordings will return an ArrayList of type string containing the contents of the file
	 * BadRecordingList.txt
	 * @return
	 */
	public ArrayList<String> getBadRecordings() {
		Scanner s = null;
		try {
			s = new Scanner(new File("BadRecordingList.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> badlist = new ArrayList<String>();
		while (s.hasNext()) {
			badlist.add(s.next());
		}
		s.close();

		return badlist;
	}

	/**
	 * The method addToPlayList is a callback function that is executed when the button 'Add to PlayList' is clicked.
	 * The method checks if the current name supplied by the user exists in the database. If the name exists, the name
	 * will be added to a listview, else an alert will be shown informing the user that the name they have provided
	 * isn't valid.
	 */
	@FXML
	public void addToPlayList() {
		String name = userName.getText();
		if (name.length() > 0) {
			name = name.replace("-", " ");
			String[] singleName = name.split(" ");

			Boolean nameExists = true;

			for (String ss : singleName) {
				System.out.println("SSSSS  " + ss);

				for (String database : databaseList) {
					if (!nameArrayList.contains(ss)) {

						nameExists = false;
					}
				}
			}
			if (nameExists) {
				playList.getItems().add(userName.getText());
				_practiceSelection.add(userName.getText());
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION,
						"This name doesn't exist in our database, please add another name.", ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					alert.close();
				}
			}
			userName.clear();
		}

	}

	/**
	 * A getter method to get the current items in the listview playList
	 * @return
	 */
	public ObservableList<String> getSelectedNames() {
		return playList.getItems();
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
	 * A callback function executed when the button 'Save Playlist' is clicked. This method will take all the items
	 * in the listview playList, and save these items in a .txt file on the user's system.
	 */
	@FXML
	public void handleExportPlayListButton() {
		for (String name : _practiceSelection) {
			BashCommandWorker creationDirectoryWorker = new BashCommandWorker(
					"name='" + name + "'\n" + "\n" + "if ! grep -qF \"$name\" UserPlayList.txt ; then "
							+ "echo \"$name\" >> UserPlayList.txt ; " + "fi");
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION, "DONE! - check in local folder", ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			alert.close();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseNames = new ArrayList<String>();
		BashCommandWorker settingUpDatabaseWorker = new BashCommandWorker(
				"mkdir User-Recordings;\n" + "mkdir Concat-Recordings;\n" + "cd Database;\n" + "rename 'y/A-Z/a-z/' *\n"
						+ "\n" + "for i in $(ls); do\n" + "\n" + "names=$(echo $i | awk -F\"_\" '{print $NF}')\n"
						+ "nameWithourExtension=\"${names%.*}\"\n" + "    echo \"This is i $i \"\n"
						+ "    mkdir \"$nameWithourExtension\"\n" + "    \n" + "    cd \"$nameWithourExtension\"\n"
						+ "    \n" + "    mkdir \"Database-Recordings\"\n" + "    \n"
						+ "    mkdir \"User-Recordings\"\n" + "\n" + "    mkdir \"Ratings\"\n" + "    \n"
						+ "    mv \"../$i\" \"./Database-Recordings\"\n" + "\n" + "    cd ..\n" + "\n" + "done\n");

		_creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		list = FXCollections.observableArrayList();
		_creationList.setItems(list);

		_creationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			/**
			 * A callback function for _creationList that will add whatever item is clicked to the current input in the text field
			 */
			@Override
			public void handle(MouseEvent event) {
				String output;
				List<Integer> indexOfDashes = new ArrayList<Integer>();
				nameList = new ArrayList<String>();
				currentName = _creationList.getSelectionModel().getSelectedItem();

				// Check if the user has begun to search for a new name in the database
				if (userName.getText().isEmpty() || userName.getText() == null || userName.getText().charAt(userName.getText().length() - 1) == ' '
						|| userName.getText().trim().charAt(userName.getText().length() - 1) == '-') {
					userName.setText(userName.getText() + currentName);
				}

				String name = userName.getText();
				//Store the indices where the char '-' appears
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
				
				// If the user is mid way in searching for a new name to add and clicks on a name, replace what they are current searching for
				if (!nameArrayList.contains(nameList.get(nameList.size() - 1))) {
					nameList.remove(nameList.size() - 1);
					nameList.add(currentName);
				}

				output = String.join(" ", nameList) + " ";
				StringBuilder finalOutput = new StringBuilder(output);

				for(Integer index: indexOfDashes) {
					//Replace spaces with '-' at the indices we stored previously
					finalOutput.setCharAt(index.intValue(), '-');
				}

				userName.setText(finalOutput.toString());
				System.out.println("This is the name of the customer " + currentName);
			}
		});
		

		playList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			/**
			 * A callback function so store the last clicked item on the listview playList
			 */
			@Override
			public void handle(MouseEvent event) {
				nameToDelete = playList.getSelectionModel().getSelectedItem();
			}
		});


		userName.textProperty().addListener(new ChangeListener<Object>() {

			/**
			 * A call back function which takes the current input of the textField and calls a method search with this current input
			 * @param observable
			 * @param oldValue
			 * @param newValue
			 */
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

				String name = userName.getText().trim();
				name = name.replace("-", " ");
				System.out.println(name);
				
				//Conditional statements to find out if a user was searching for a new name when they clicked on an item
				if (name == null || name.isEmpty() || !name.contains(" ")) {
					search((String) name);
				} else {
					search((String) name.substring(name.lastIndexOf(" "), name.length()).trim());
					System.out.println(
							"We're searching for " + name.substring(name.lastIndexOf(" "), name.length()).trim());
				}

			}

		});
	}

	/**
	 * A callback function called when the button 'Clear Playlist' is clicked. This method will clear the current items in the listview
	 * playList after receiving confirmation in the form of an alert from the user.
	 */
	@FXML
	public void clearPlayList(){
		if (!_practiceSelection.isEmpty()) {

			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Do you want to clear your playlist", ButtonType.NO, ButtonType.YES);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				playList.getItems().clear();
				_practiceSelection.clear();
			}
			alert.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plesase add names to your playlist.", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}
	}

	/**
	 * A callback function called when the button 'Delete' is clicked. This method will delete the current selected item in the listview
	 * playList after receiving confirmation in the form of an alert from the user.
	 */
	@FXML
	public void deleteSelectedPlayListName(){
		if (playList.getSelectionModel().getSelectedItem().equals(null)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select name in your playlist to delete", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Would you like to delete your selected name?.", ButtonType.NO, ButtonType.YES);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				playList.getItems().remove(playList.getSelectionModel().getSelectedItem());
				_practiceSelection.remove(playList.getSelectionModel().getSelectedItem());

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

		if (userName.getText() != null && (nameToSearch.isEmpty())) {
			_creationList.setItems(list);
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

		_creationList.setItems(finalList);
	}


	/**
	 * Helper method to read recordings from the database folder
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
						String databaseName = line;
						databaseList.add(line);
						// System.out.println("This is the recording file " +line);
						line = line.substring(0, line.length() - 4);
						line = line.substring(line.lastIndexOf('_') + 1);
						nameArrayList.add(line);
						// System.out.println("This is the ds file " +line);
						list.add(line);

					}
					stdoutBuffered.close();
					ArrayList<String> namesBadRecordings = new ArrayList<>();
					System.out.println("This is the database list ");
					for (String actualName : getBadRecordings()) {
						actualName = actualName.substring(0, actualName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						namesBadRecordings.add(actualName);
						System.out.println("acsdfdsfastas " + actualName);
					}
					System.out.println(namesBadRecordings);

					for (String databaseName : databaseList) {
						// System.out.println("This is the database list " + databaseName);
						String actualName = databaseName.substring(0, databaseName.length() - 4);
						actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
						// System.out.println("This is the actual name " + actualName);
						int occurrences = Collections.frequency(nameArrayList, actualName);
						System.out.println("Occurence " + occurrences);
						System.out.println("actas " + Collections.frequency(namesBadRecordings, actualName));

						if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))) {
							if (!getBadRecordings().contains(databaseName)) {
								Names.put(actualName, databaseName);
							}
						} else {
							Names.put(actualName, databaseName);

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

	/**
	 * This method will return an ArrayList of type string containing all names which have been rated as bad.
	 * @return
	 */
	public ArrayList<String> actualNamesInBadRecordings() {
		for (String actualName : getBadRecordings()) {
			actualNamesBadRecordings.add(actualName);
		}
		return actualNamesBadRecordings;
	}
	/**
	 * This method will add to the field actualNames all the recordings which haven't been flagged as bad
	 */
	public void selectGoodRecordings() {
		ArrayList<String> namesBadRecordings = new ArrayList<>();
		System.out.println("This is the database list ");
		for (String actualName : getBadRecordings()) {
			actualName = actualName.substring(0, actualName.length() - 4);
			actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
			namesBadRecordings.add(actualName);
			System.out.println("acsdfdsfastas " + actualName);
		}

		System.out.println(namesBadRecordings);

		for (String databaseName : databaseList) {
			// System.out.println("This is the database list " + databaseName);
			String actualName = databaseName.substring(0, databaseName.length() - 4);
			actualName = actualName.substring(actualName.lastIndexOf('_') + 1);
			// System.out.println("This is the actual name " + actualName);
			int occurrences = Collections.frequency(nameArrayList, actualName);
			System.out.println("Occurence " + occurrences);
			System.out.println("actas " + Collections.frequency(namesBadRecordings, actualName));

			if (occurrences > 1 && (occurrences != Collections.frequency(namesBadRecordings, actualName))) {
				if (!getBadRecordings().contains(databaseName)) {
					actualNames.put(actualName, databaseName);
				}
			} else {
				actualNames.put(actualName, databaseName);

			}
		}
		Iterator it = actualNames.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (pair.getKey().equals("li")) {
				System.out.println(pair.getKey() + " = " + pair.getValue());
				it.remove(); // avoids a ConcurrentModificationException
			}
		}

	}

	/**
	 * 
	 * @param databaseName
	 * @param realName
	 */
	public static void addingNewDBRecording(String databaseName, String realName) {
		databaseList.add(databaseName);
		nameArrayList.add(realName);
		list.add(realName);
		Names.put(realName, databaseName);
	}

	/**
	 * Helper method which gets the items from the _practiceSelection
	 */
	public static ObservableList<String> getItemList() {
		ObservableList<String> items = FXCollections.observableArrayList();
		for (String name : _practiceSelection) {
			System.out.println("This is the name " + name);
			String[] individualNames = name.split(" ");
			if (name.contains(" ")) {
				List<File> listOfFiles = new ArrayList<>();
				for (String ss : individualNames) {
					System.out.println("Individual names " + ss);
					System.out.println("Database names " + Names.get(ss));

					File file = new File("./Database/" + ss + "/Database-Recordings/" + Names.get(ss));

					listOfFiles.add(file);

				}
				// createConcatFile(listOfFiles, "./Concat-Recordings/" + name.replaceAll(" ",
				// "_"));
			}
			items.add(name);
			System.out.println("Items " + items);
		}

		return items;
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
	public static ObservableList<String> getNames() {
		System.out.println(list);

		return list;
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
	 * Method will take in a list of .wav files and concatenat them into one .wav
	 * file and return it
	 *
	 * @param listOfFiles
	 * @return concat audio file
	 */
	public static File createConcatFile(List<File> listOfFiles, String name) {

		String command = "ffmpeg -y";

		for (File file : listOfFiles) {
			String filename = file.getPath().substring(0, file.getPath().lastIndexOf('.'));
			System.out.println(filename);

			command = command + " -i " + file.getPath();
		}

		command = command + " -filter_complex '[0:0][1:0]concat=n=" + listOfFiles.size()
				+ ":v=0:a=1[out]' -map '[out]' " + name + ".wav";

		System.out.println(command);

		ProcessBuilder concatBuilder = new ProcessBuilder("/bin/bash", "-c", command);

		try {
			Process concatProcess = concatBuilder.start();
			concatProcess.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		removeSilence(new File("Concat-Recordings/" + name + ".wav"));

		normalizeAudio(new File("Concat-Recordings/" + name + ".wav"));

		return new File(name + ".wav");
	}

	public static void removeSilence(File file) {
		ProcessBuilder whitenoiseBuilder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -hide_banner -i "
				+ file.getPath() + " -af " + "silenceremove=1:0:-35dB:1:5:-35dB:0 " + file.getPath());

		try {
			Process whitenoiseProcess = whitenoiseBuilder.start();
			whitenoiseProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void normalizeAudio(File file) {
		ProcessBuilder normalizeBuilder = new ProcessBuilder("/bin/bash", "-c",
				"ffmpeg -y -i " + file.getPath() + " -af dynaudnorm " + file.getPath());
		Process normalize;
		try {
			normalize = normalizeBuilder.start();
			normalize.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}