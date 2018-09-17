package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage _primaryStage;
    private static Scene _mainMenu,_practiceMenu,_databaseMenu;

    @Override
    public void start(Stage primaryStage) throws Exception{
        _primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Parent practiceRoot = FXMLLoader.load(getClass().getResource("DataBase.fxml"));
        primaryStage.setTitle("Namesayer");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        _practiceMenu = new Scene(practiceRoot,300,275);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void changeToDataBase() {
        _primaryStage.setScene(_practiceMenu);
    }
}
