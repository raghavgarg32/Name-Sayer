package controllers;

import java.io.File;

import helpers.BashCommandWorker;
import helpers.PlayRecordings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Sets up the initial scene of the application
 * This application uses a free CSS library called material.css
 */
public class Main extends Application {

    private static Stage _primaryStage;


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
    private static Scene _loadingMenu;



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
    private static FXMLLoader loadingLoader;



    /**
     * This method sets up all of the scenes and allows program to change scene
     * @param primaryStage
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        _primaryStage = primaryStage;
        _primaryStage.setResizable(false);

        File userRecordDir = new File(System.getProperty("user.dir")+"/User-Recordings");
        if (!userRecordDir.exists()){
            userRecordDir.mkdir();
        }


        File conctCatRecordDir = new File(System.getProperty("user.dir")+"/Concat-Recordings");
        if (!conctCatRecordDir.exists()){
            conctCatRecordDir.mkdir();
        }

        File badRecordingList = new File(System.getProperty("user.dir")+"/BadRecordingList.txt");
        if(!badRecordingList.exists()) {
            badRecordingList.createNewFile();
        }


        practiceLoader = new FXMLLoader();
        practiceLoader.setLocation(getClass().getResource("/views/PracticeView.fxml"));
        Parent practiceRoot = practiceLoader.load();

        recordLoader = new FXMLLoader();
        recordLoader.setLocation(getClass().getResource("/views/RecordView.fxml"));
        Parent recordRoot = recordLoader.load();

        confirmLoader = new FXMLLoader();
        confirmLoader.setLocation(getClass().getResource("/views/UserRecordingConfirmView.fxml"));
        Parent confirmRoot = confirmLoader.load();

        rateLoader = new FXMLLoader();
        rateLoader.setLocation(getClass().getResource("/views/RateView.fxml"));
        Parent rateMenuRoot = rateLoader.load();

        helpLoader = new FXMLLoader();
        helpLoader.setLocation(getClass().getResource("/views/HelpPageView.fxml"));
        Parent helpMenuRoot = helpLoader.load();

        rewardLoader = new FXMLLoader();
        rewardLoader.setLocation(getClass().getResource("/views/RewardMenu.fxml"));
        Parent rewardMenuRoot = rewardLoader.load();

        userRecordingsLoader = new FXMLLoader();
        userRecordingsLoader.setLocation(getClass().getResource("/views/UserRecordingsView.fxml"));
        Parent userRecordingsMenuRoot = userRecordingsLoader.load();

        dbRecordingsLoader = new FXMLLoader();
        dbRecordingsLoader.setLocation(getClass().getResource("/views/DBRecordingsView.fxml"));
        Parent dbRecordingsLoaderRoot = dbRecordingsLoader.load();

        addDBRecordingsLoader = new FXMLLoader();
        addDBRecordingsLoader.setLocation(getClass().getResource("/views/AddDBRecordingsView.fxml"));
        Parent addDBRecordingsLoaderRoot = addDBRecordingsLoader.load();

        confirmDBRecordingsLoader = new FXMLLoader();
        confirmDBRecordingsLoader.setLocation(getClass().getResource("/views/DBRecordingConfirmView.fxml"));
        Parent confirmDBRecordingsLoaderRoot = confirmDBRecordingsLoader.load();

        loadingLoader = new FXMLLoader();
        loadingLoader.setLocation(getClass().getResource("/views/LoadView.fxml"));
        Parent loadingRoot = loadingLoader.load();


        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("/views/HomeView.fxml"));
        Parent micTestRoot = FXMLLoader.load(getClass().getResource("/views/TestMic.fxml"));

        _dataBaseMenu = new Scene(dataBaseRoot,1157,767);
        _practiceMenu = new Scene(practiceRoot,1157,767);
        _recordMenu = new Scene(recordRoot,1157,767);
        _confirmMenu = new Scene(confirmRoot,1157,767);
        _micTestMenu = new Scene(micTestRoot,1157,767);
        _rateMenu = new Scene(rateMenuRoot,1157,767);
        _rewardMenu = new Scene(rewardMenuRoot,1157,767);
        _helpMenu = new Scene(helpMenuRoot,1157,767);
        _userRecordingsMenu = new Scene(userRecordingsMenuRoot,1157,767);
        _dbRecordingsMenu = new Scene(dbRecordingsLoaderRoot,1157,767);
        _addDBRecordingsMenu = new Scene(addDBRecordingsLoaderRoot,1157,767);
        _confirmDBRecordingsMenu = new Scene(confirmDBRecordingsLoaderRoot,1157,767);
        _loadingMenu = new Scene(loadingRoot,1041,767);


        primaryStage.setTitle("Namesayer");
        primaryStage.setScene(_loadingMenu);
        LoadViewController controller = loadingLoader.getController();
        controller.load();
        primaryStage.show();




    }

    @Override
    public void stop(){
        BashCommandWorker removeConcatRecordings = new BashCommandWorker("rm -r Concat-Recordings;");
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
        PracticeViewController controller = practiceLoader.getController();
        controller.names(HomeViewController.getNamesForPracticeObservableList());
        controller.settingUserListView(PracticeViewController.getCurrentName(false));
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_practiceMenu);
    }

    /**
     * Changes scene to record scene
     */
    public static void changeSceneRecord() {
        RecordViewController controller = recordLoader.getController();
        controller.getNameForRecording(PracticeViewController.getCurrentName(AddDBRecordingsViewController.getIsRecordingForDB()));
        controller.initScene();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_recordMenu);

    }

    /**
     * Changes scene to Database scene
     */
    public static void changeSceneDataBase() {
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_dataBaseMenu);
    }

    /**
     * Changes scene to Confirm scene
     */
    public static void changeSceneConfirm() {
        UserRecordingConfirmViewController controller = confirmLoader.getController();
        controller.setNameLabel(PracticeViewController.getCurrentName(false));
        controller.setLoopNumber();
        _primaryStage.setScene(_confirmMenu);
        PlayRecordings.stopPlayRecording();
    }

    /**
     * Changes scene to Mic Test scene
     */
    public static void changeSceneMicTest() {
        RecordViewController controller = recordLoader.getController();
        controller.initScene();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_micTestMenu);
    }

    /**
     * Changes scene to the Help Scene
     */
    public static void changeSceneHelpMenu() {
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_helpMenu);
    }


    /**
     * Changes scene to Rate Menu scene
     */
    public static void changeSceneRateMenu() {
        RateViewController controller = rateLoader.getController();
        controller.setUp();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_rateMenu);
    }

    /**
     * Changes scene to the reward scene
     */
    public static void changeSceneRewardMenu() {
        RewardMenuController controller = rewardLoader.getController();
        controller.updateButtonStatus();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_rewardMenu);
    }

    /**
     * Changes scene to the user recording scene
     */
    public static void changeSceneUserRecordingsMenu () {
        UserRecordingsViewController controller = userRecordingsLoader.getController();
        controller.settingUserListView();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_userRecordingsMenu);
    }

    /**
     * Changes scene to the database recording scene
     */
    public static void changeSceneDBRecordingsMenu () {
        DBRecordingsViewController controller = dbRecordingsLoader.getController();
        controller.settingDBListView();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_dbRecordingsMenu);
    }

    /**
     * Changes scene to the add database recordings scene
     */
    public static void changeSceneAddDBRecordingsMenu () {
        AddDBRecordingsViewController controller = addDBRecordingsLoader.getController();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_addDBRecordingsMenu);
    }

    /**
     * Changes scene to the confirm database recording scene
     */
    public static void changeSceneConfrimDBRecordingsMenu () {
        DBRecordingConfirmViewController controller = confirmDBRecordingsLoader.getController();
        controller.setNameLabel(AddDBRecordingsViewController.getCurrentDBName());
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_confirmDBRecordingsMenu);
    }
}