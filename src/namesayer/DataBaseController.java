package namesayer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class DataBaseController implements Initializable {

    public static List<String> _practiceSelection = new ArrayList<>();

    @FXML
    private ListView<String> _creationList;

    @FXML
    private Button practiceBtn;


    @FXML
    public void handlePracticeBtn() {
        if(_practiceSelection.size() > 1) {
            Alert randomizeAlert = new Alert(Alert.AlertType.INFORMATION, "Would you like" +
                    " to randomise your selection",ButtonType.NO, ButtonType.YES);
            randomizeAlert.showAndWait();
            if(randomizeAlert.getResult() == ButtonType.YES) {
                Collections.shuffle(_practiceSelection);
                randomizeAlert.close();
                Main.changeScenePractice();
            }
            else if(randomizeAlert.getResult() == ButtonType.NO) {
                randomizeAlert.close();
                Main.changeScenePractice();
            }
        }
        else if(_practiceSelection.size() == 1) {
       	 Main.changeScenePractice();
       }	
        else if(_practiceSelection.size() == 0) {
            Alert selectionAlert = new Alert(Alert.AlertType.INFORMATION, "Please select " +
                    "something" ,ButtonType.OK);
            selectionAlert.showAndWait();
            if(selectionAlert.getResult() == ButtonType.OK) {
                selectionAlert.close();
            }
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    	
    	
        
        _creationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        
        _creationList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty itemState = new SimpleBooleanProperty();
                itemState.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected == true) {
                        _practiceSelection.add(item);
                    }
                    else if(isNowSelected == false) {
                        _practiceSelection.remove(item);
                    }
                });
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
