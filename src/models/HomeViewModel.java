package models;

import helpers.Alerts;
import helpers.BashCommandWorker;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

/**
 * This is the model for the home view controllers, handles the uploading and the exporting of the playlist
 */
public class HomeViewModel {

    /**
     * upload is a callback function executed when the button 'Upload playlist' is clicked. This method allows a user to navigate
     * their file system and upload a .txt file. The contents of the .txt file should be names stored in the database. The method will
     * populate the listview with the names in the .txt file. If any names don't exist in the database, the method will throw an alert
     * and the user will be informed of this. However, even though an alert is thrown, any names in the .txt file which exist in the database
     * will be added to the listview
     */

    public void upload(List list, List practiceSelection, ListView playList) {
       List<String> fileNames = new ArrayList<>();
       FileChooser fileChooser = new FileChooser();
       fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
       File selectedFile = fileChooser.showOpenDialog(null);
       if (selectedFile != null) {
           
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
               
               

               for (String name : fileNames) {
                   if (!name.trim().equals("")) {
                       String[] individualNames = name.split("[-\\s]");

                       ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(individualNames));
                       if (list.containsAll(arrayList)) {
                           playList.getItems().add(name);
                           practiceSelection.add(name);
                       } else {
                           Alerts.show("This text file contains names that aren't in the database, those names will not be added to review", ButtonType.OK, null);
                       }
                   }
               }

               
               
           } catch (IOException e) {

           }
       }
   }

    /**
     * A callback function executed when the button 'Save Playlist' is clicked. This method will take all the items
     * in the listview playList, and save these items in a .txt file on the user's system.
     */
    public void exportPlayListButton(List<String> practiceSelection) {

        for (String name : practiceSelection) {
            BashCommandWorker creationDirectoryWorker = new BashCommandWorker(
                    "name='" + name + "'\n" + "\n" + "if ! grep -qF \"$name\" UserPlayList.txt ; then "
                            + "echo \"$name\" >> UserPlayList.txt ; " + "fi");
        }
        Alerts.show("DONE! - check in local folder for file UserPlayList.txt", ButtonType.OK, null);
    }

}
