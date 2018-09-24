package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;


/**
 * Sets up the initial scene of the application
 */
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
    private static FXMLLoader confirmLoader;


    /**
     * This method sets up all of the scenes and allows program to change scene
     * @param primaryStage
     * @throws Exception
     */

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

        confirmLoader = new FXMLLoader();
        confirmLoader.setLocation(getClass().getResource("ConfirmView.fxml"));
        Parent confirmRoot = confirmLoader.load();

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("DataBase.fxml"));
        Parent micTestRoot = FXMLLoader.load(getClass().getResource("TestMic.fxml"));
        Parent rateMenuRoot = FXMLLoader.load(getClass().getResource("RateView.fxml"));

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

    /**
     * Launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Changes scene to main scene
     */
    public static void changeSceneMain() {
        _primaryStage.setScene(_mainMenu);
    }

    /**
     * Changes scene to practice scene
     */
    public static void changeScenePractice() {
        PracticeMenuController controller = practiceLoader.getController();
        controller.names(DataBaseController.getItemList(),DataBaseController.getNamesWithoutNumbers(),
                DataBaseController.getNamesWithNumbers());
        controller.userListView();
        _primaryStage.setScene(_practiceMenu);
    }

    /**
     * Changes scene to record scene
     */
    public static void changeSceneRecord() {
        RecordView controller = recordLoader.getController();
        controller.getNameForRecording(PracticeMenuController.getCurrentNameWithoutNumber());
        controller.initScene();
        _primaryStage.setScene(_recordMenu);
    }

    /**
     * Changes scene to save scene
     */
    public static void changeSceneSave() {
        SaveViewController controller = saveLoader.getController();
        controller.setsPromptText();
        _primaryStage.setScene(_saveMenu);
    }

    /**
     * Changes scene to Database scene
     */
    public static void changeSceneDataBase() {
        _primaryStage.setScene(_dataBaseMenu);
    }

    /**
     * Changes scene to Confirm scene
     */
    public static void changeSceneConfirm() {
        ConfirmView controller = confirmLoader.getController();
        controller.setNameLabel(PracticeMenuController.getCurrentNameWithoutNumber());
        _primaryStage.setScene(_confirmMenu);
    }

    /**
     * Changes scene to Mic Test scene
     */
    public static void changeSceneMicTest() {
        _primaryStage.setScene(_micTestMenu);
    }

    /**
     * Changes scene to Rate Menu scene
     */
    public static void changeSceneRateMenu() {
        _primaryStage.setScene(_rateMenu);
    }

}