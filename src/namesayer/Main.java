package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Sets up the initial scene of the application
 */
public class Main extends Application {

    private static Stage _primaryStage;

    //add any new scene as a field here
    private static Scene _dataBaseMenu;
    private static Scene _recordMenu;
    private static Scene _practiceMenu;
    private static Scene _confirmMenu;
    private static Scene _micTestMenu;
    private static Scene _rateMenu;
    private static Scene _rewardMenu;
    private static Scene _helpMenu;
    private static Scene _userRecordingsMenu;
    private static Scene _dbRecordingsMenu;
    private static Scene _addDBRecordingsMenu;
    private static Scene _confirmDBRecordingsMenu;


    private static FXMLLoader practiceLoader;
    private static FXMLLoader recordLoader;
    private static FXMLLoader confirmLoader;
    private static FXMLLoader rateLoader;
    private static FXMLLoader rewardLoader;
    private static FXMLLoader helpLoader;
    private static FXMLLoader userRecordingsLoader;
    private static FXMLLoader dbRecordingsLoader;
    private static FXMLLoader addDBRecordingsLoader;
    private static FXMLLoader confirmDBRecordingsLoader;


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

        confirmLoader = new FXMLLoader();
        confirmLoader.setLocation(getClass().getResource("UserRecordingConfirmView.fxml"));
        Parent confirmRoot = confirmLoader.load();

        rateLoader = new FXMLLoader();
        rateLoader.setLocation(getClass().getResource("RateView.fxml"));
        Parent rateMenuRoot = rateLoader.load();

        helpLoader = new FXMLLoader();
        helpLoader.setLocation(getClass().getResource("HelpPage.fxml"));
        Parent helpMenuRoot = helpLoader.load();

        rewardLoader = new FXMLLoader();
        rewardLoader.setLocation(getClass().getResource("RewardMenu.fxml"));
        Parent rewardMenuRoot = rewardLoader.load();

        userRecordingsLoader = new FXMLLoader();
        userRecordingsLoader.setLocation(getClass().getResource("UserRecordingsView.fxml"));
        Parent userRecordingsMenuRoot = userRecordingsLoader.load();

        dbRecordingsLoader = new FXMLLoader();
        dbRecordingsLoader.setLocation(getClass().getResource("DBRecordingsView.fxml"));
        Parent dbRecordingsLoaderRoot = dbRecordingsLoader.load();

        addDBRecordingsLoader = new FXMLLoader();
        addDBRecordingsLoader.setLocation(getClass().getResource("AddDBRecordingsView.fxml"));
        Parent addDBRecordingsLoaderRoot = addDBRecordingsLoader.load();

        confirmDBRecordingsLoader = new FXMLLoader();
        confirmDBRecordingsLoader.setLocation(getClass().getResource("DBRecordingConfirmView.fxml"));
        Parent confirmDBRecordingsLoaderRoot = confirmDBRecordingsLoader.load();


        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("DataBase.fxml"));
        Parent micTestRoot = FXMLLoader.load(getClass().getResource("TestMic.fxml"));

        _dataBaseMenu = new Scene(dataBaseRoot,959,773);
        _practiceMenu = new Scene(practiceRoot,925,634);
        _recordMenu = new Scene(recordRoot,925,634);
        _confirmMenu = new Scene(confirmRoot,925,634);
        _micTestMenu = new Scene(micTestRoot,925,634);
        _rateMenu = new Scene(rateMenuRoot,925,634);
        _rewardMenu = new Scene(rewardMenuRoot,925,634);
        _helpMenu = new Scene(helpMenuRoot,925,634);
        _userRecordingsMenu = new Scene(userRecordingsMenuRoot,925,634);
        _dbRecordingsMenu = new Scene(dbRecordingsLoaderRoot,925,634);
        _addDBRecordingsMenu = new Scene(addDBRecordingsLoaderRoot,925,634);
        _confirmDBRecordingsMenu = new Scene(confirmDBRecordingsLoaderRoot,925,634);


        primaryStage.setTitle("Namesayer");
        primaryStage.setScene(_dataBaseMenu);
        primaryStage.show();


    }

    @Override
    public void stop(){
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm -rf Concat-Recordings;");

    }

    /**
     * Launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Changes scene to practice scene
     */
    public static void changeScenePractice() {
        PracticeMenuController controller = practiceLoader.getController();
        controller.names(DataBaseController.getItemList());
        controller.settingUserListView(PracticeMenuController.getSelectedName());
        _primaryStage.setScene(_practiceMenu);
    }

    /**
     * Changes scene to record scene
     */
    public static void changeSceneRecord() {
        RecordView controller = recordLoader.getController();
        controller.getNameForRecording(PracticeMenuController.getCurrentNameWithoutNumber(AddDBRecordingsViewController.getRecordingForDB()));
        controller.initScene();
        _primaryStage.setScene(_recordMenu);
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
        UserRecordingConfirmView controller = confirmLoader.getController();
        controller.setNameLabel(PracticeMenuController.getCurrentNameWithoutNumber(false));
        _primaryStage.setScene(_confirmMenu);
    }

    /**
     * Changes scene to Mic Test scene
     */
    public static void changeSceneMicTest() {
        _primaryStage.setScene(_micTestMenu);
    }

    public static void changeSceneHelpMenu() {
        _primaryStage.setScene(_helpMenu);
    }


    /**
     * Changes scene to Rate Menu scene
     */
    public static void changeSceneRateMenu() {
        RateViewController controller = rateLoader.getController();
        controller.setlabel();
        _primaryStage.setScene(_rateMenu);
    }

    public static void changeSceneRewardMenu() {
        RewardMenuController controller = rewardLoader.getController();
        controller.updateButtonStatus();
        _primaryStage.setScene(_rewardMenu);
    }

    public static void changeSceneUserRecordingsMenu () {
        UserRecordingsViewController controller = userRecordingsLoader.getController();
        controller.settingUserListView();
        _primaryStage.setScene(_userRecordingsMenu);
    }

    public static void changeSceneDBRecordingsMenu () {
        DBRecordingsViewController controller = dbRecordingsLoader.getController();
        controller.settingDBListView();
        _primaryStage.setScene(_dbRecordingsMenu);
    }

    public static void changeSceneAddDBRecordingsMenu () {
        AddDBRecordingsViewController controller = addDBRecordingsLoader.getController();
        _primaryStage.setScene(_addDBRecordingsMenu);
    }

    public static void changeSceneConfrimDBRecordingsMenu () {
        DBRecordingConfirmView controller = confirmDBRecordingsLoader.getController();
        _primaryStage.setScene(_confirmDBRecordingsMenu);
    }
}