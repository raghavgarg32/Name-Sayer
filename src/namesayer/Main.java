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
    private static Scene _confirmMenu;
    private static Scene _micTestMenu;
    private static Scene _rateMenu;
    private static Scene _saveMenu;

    private static FXMLLoader practiceLoader;
    private static FXMLLoader recordLoader;
    private static FXMLLoader saveLoader;




    @Override
    public void start(Stage primaryStage) throws Exception{
        _primaryStage = primaryStage;
        practiceLoader = new FXMLLoader();
        practiceLoader.setLocation(getClass().getResource("PracticeMenu.fxml"));
        Parent practiceRoot = practiceLoader.load();

        recordLoader = new FXMLLoader();
        recordLoader.setLocation(getClass().getResource("RecordView.fxml"));
        Parent recordRoot = recordLoader.load();

        saveLoader = new FXMLLoader();
        saveLoader.setLocation(getClass().getResource("SaveView.fxml"));
        Parent saveRoot = saveLoader.load();

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("DataBase.fxml"));
        Parent confirmRoot = FXMLLoader.load(getClass().getResource("ConfirmView.fxml"));
        Parent micTestRoot = FXMLLoader.load(getClass().getResource("TestMic.fxml"));
        Parent rateMenuRoot = FXMLLoader.load(getClass().getResource("RateView.fxml"));

        //Create the directory to put creations in
        File creationDir = new File(System.getProperty("user.dir")+"/Creations");
        if (!creationDir.exists()){
            creationDir.mkdir();
        }

        _mainMenu = new Scene(root,600,400);
        _dataBaseMenu = new Scene(dataBaseRoot,600,400);
        _practiceMenu = new Scene(practiceRoot,600,400);
        _recordMenu = new Scene(recordRoot,600,400);
        _confirmMenu = new Scene(confirmRoot,600,400);
        _micTestMenu = new Scene(micTestRoot,600,400);
        _rateMenu = new Scene(rateMenuRoot,600,400);
        _saveMenu = new Scene(saveRoot,600,400);

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
        PracticeMenuController controller = practiceLoader.getController();
        controller.names(DataBaseController.getItemList(),DataBaseController.getNamesWithoutNumbers(),
                DataBaseController.getNamesWithNumbers());
        controller.userListView();
        _primaryStage.setScene(_practiceMenu);
    }

    public static void changeSceneRecord() {
        RecordView controller = recordLoader.getController();
        controller.getNameForRecording(PracticeMenuController.getCurrentName());
        _primaryStage.setScene(_recordMenu);
    }

    public static void changeSceneSave() {
        _primaryStage.setScene(_saveMenu);
    }

    public static void changeSceneDataBase() {
        _primaryStage.setScene(_dataBaseMenu);
    }
    
    public static void changeSceneConfirm() {
        _primaryStage.setScene(_confirmMenu);
    }
    
    public static void changeSceneMicTest() {
        _primaryStage.setScene(_micTestMenu);
    }

    public static void changeSceneRateMenu() {
    	_primaryStage.setScene(_rateMenu);
    }




}