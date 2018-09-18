package namesayer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DataBaseController implements Initializable {

    private List<String> _practiceSelection = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    @FXML
    private Button practiceBtn;


    @FXML
    public void handlePracticeBtn() {
        List<String> selectedItems = _creationList.getSelectionModel().getSelectedItems();


        System.out.println(selectedItems);
        Main.changeScenePractice();
    }


    // Trying to get this method to display .mp4 files in the Creations folder but its not working for some reason?
    public void listCreations() {
        File dir = new File("Creations/");
        List<String> fileList = new ArrayList<>();
        File[] directoryListing = dir.listFiles();
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".mp4");
            }
        };
        //Loop through files and store .mp4 files in a list
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(fileFilter.accept(dir, child.getName())) {
                    fileList.add(child.getName());
                }
            }
        }
        ObservableList<String> items = FXCollections.observableArrayList(fileList);
        _creationList.setItems(items);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listCreations();
        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //this code gets the check boxes but i dont know how to  work with it
        _creationList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty itemState = new SimpleBooleanProperty();
                itemState.addListener((obs, wasSelected, isNowSelected) ->
                        System.out.println("Check box for "+item+" changed from "+wasSelected+" to "+isNowSelected)
                );
                return itemState;
            }
        }));


        // This is just test data for the list
        ObservableList<String> list = FXCollections.observableArrayList();
        _creationList.setItems(list);
        list.add("item1");
        list.add("item2");
        list.add("item3");
    }
}
