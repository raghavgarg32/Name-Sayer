package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Controller {
    @FXML
    Button btn;
    @FXML
    public void handleButton() throws IOException {
        System.out.print("Hello");
        Parent root = FXMLLoader.load(getClass().getResource("RecordView.fxml"));
        Stage primaryStage = (Stage) btn.getScene().getWindow();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 473, 375));
        primaryStage.show();

    }
}
