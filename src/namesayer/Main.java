package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static Stage _primaryStage;

    //add any new scene as a field here
    private static Scene _dataBaseMenu;
    private static Scene _mainMenu;
    private static Scene _recordMenu;
    private static Scene _practiceMenu;





    @Override
    public void start(Stage primaryStage) throws Exception{
        _primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("DataBase.fxml"));
        Parent practiceRoot = FXMLLoader.load(getClass().getResource("PracticeMenu.fxml"));
        Parent recordRoot = FXMLLoader.load(getClass().getResource("RecordView.fxml"));

        //Create the directory to put creations in
        File creationDir = new File(System.getProperty("user.dir")+"/Creations");
        if (!creationDir.exists()){
            creationDir.mkdir();
        }

        _mainMenu = new Scene(root,473,375);
        _dataBaseMenu = new Scene(dataBaseRoot,473,375);
        _practiceMenu = new Scene(practiceRoot,473,375);
        _recordMenu = new Scene(recordRoot,473,375);

        primaryStage.setTitle("Namesayer");
        primaryStage.setScene(_mainMenu);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Methods to change scene go here
     */

    public static void changeSceneMain() {
        _primaryStage.setScene(_mainMenu);
    }
    public static void changeScenePractice() {
        _primaryStage.setScene(_practiceMenu);
    }
    public static void changeSceneDataBase() {
        _primaryStage.setScene(_dataBaseMenu);
    }

    public static void changeSceneRecord() {
        _primaryStage.setScene(_recordMenu);
    }




}