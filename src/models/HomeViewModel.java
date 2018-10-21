package models;

import controllers.Main;
import helpers.Alerts;
import helpers.BashCommandWorker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class HomeViewModel {

   public void upload(List list, List _practiceSelection, ListView playList) {
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

                   Alerts.show("This text file contains names that aren't in the database, those names will not be added to review", ButtonType.OK, null);

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

    public void ExportPlayListButton(List<String> _practiceSelection) {

        for (String name : _practiceSelection) {
            BashCommandWorker creationDirectoryWorker = new BashCommandWorker(
                    "name='" + name + "'\n" + "\n" + "if ! grep -qF \"$name\" UserPlayList.txt ; then "
                            + "echo \"$name\" >> UserPlayList.txt ; " + "fi");
        }
        Alerts.show("DONE! - check in local folder for file UserPlayList.txt", ButtonType.OK, null);
    }

}
