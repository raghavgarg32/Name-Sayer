package Controllers;

import java.io.File;

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


        practiceLoader = new FXMLLoader();
        practiceLoader.setLocation(getClass().getResource("../Views/PracticeMenu.fxml"));
        Parent practiceRoot = practiceLoader.load();

        recordLoader = new FXMLLoader();
        recordLoader.setLocation(getClass().getResource("../Views/RecordView.fxml"));
        Parent recordRoot = recordLoader.load();

        confirmLoader = new FXMLLoader();
        confirmLoader.setLocation(getClass().getResource("../Views/UserRecordingConfirmView.fxml"));
        Parent confirmRoot = confirmLoader.load();

        rateLoader = new FXMLLoader();
        rateLoader.setLocation(getClass().getResource("../Views/RateView.fxml"));
        Parent rateMenuRoot = rateLoader.load();

        helpLoader = new FXMLLoader();
        helpLoader.setLocation(getClass().getResource("../Views/HelpPage.fxml"));
        Parent helpMenuRoot = helpLoader.load();

        rewardLoader = new FXMLLoader();
        rewardLoader.setLocation(getClass().getResource("../Views/RewardMenu.fxml"));
        Parent rewardMenuRoot = rewardLoader.load();

        userRecordingsLoader = new FXMLLoader();
        userRecordingsLoader.setLocation(getClass().getResource("../Views/UserRecordingsView.fxml"));
        Parent userRecordingsMenuRoot = userRecordingsLoader.load();

        dbRecordingsLoader = new FXMLLoader();
        dbRecordingsLoader.setLocation(getClass().getResource("../Views/DBRecordingsView.fxml"));
        Parent dbRecordingsLoaderRoot = dbRecordingsLoader.load();

        addDBRecordingsLoader = new FXMLLoader();
        addDBRecordingsLoader.setLocation(getClass().getResource("../Views/AddDBRecordingsView.fxml"));
        Parent addDBRecordingsLoaderRoot = addDBRecordingsLoader.load();

        confirmDBRecordingsLoader = new FXMLLoader();
        confirmDBRecordingsLoader.setLocation(getClass().getResource("../Views/DBRecordingConfirmView.fxml"));
        Parent confirmDBRecordingsLoaderRoot = confirmDBRecordingsLoader.load();

        loadingLoader = new FXMLLoader();
        loadingLoader.setLocation(getClass().getResource("../Views/LoadingView.fxml"));
        Parent loadingRoot = loadingLoader.load();


        Parent dataBaseRoot = FXMLLoader.load(getClass().getResource("/Views/DataBase.fxml"));
        Parent micTestRoot = FXMLLoader.load(getClass().getResource("/Views/TestMic.fxml"));

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
        LoadMenuController controller = loadingLoader.getController();
        controller.load();
        primaryStage.show();




    }

    /**
     * Method called when the application closes, it removes the directory Conct-Recordings
     */
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
        RecordView controller = recordLoader.getController();
//        controller.stopRecording();
        controller.initScene();
        _primaryStage.setScene(_micTestMenu);
    }

    public static void changeSceneHelpMenu() {
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_helpMenu);
    }


    /**
     * Changes scene to Rate Menu scene
     */
    public static void changeSceneRateMenu() {
        RateViewController controller = rateLoader.getController();
        controller.setlabel();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_rateMenu);
    }

    /**
     * Changes scene to the Reward Menu 
     */
    public static void changeSceneRewardMenu() {
        RewardMenuController controller = rewardLoader.getController();
        controller.updateButtonStatus();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_rewardMenu);
    }

    /**
     * Changes scene to the User Recording menu
     */
    public static void changeSceneUserRecordingsMenu () {
        UserRecordingsViewController controller = userRecordingsLoader.getController();
        controller.settingUserListView();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_userRecordingsMenu);
    }

    /**
     * Changes scene to the database recording menu
     */
    public static void changeSceneDBRecordingsMenu () {
        DBRecordingsViewController controller = dbRecordingsLoader.getController();
        controller.settingDBListView();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_dbRecordingsMenu);
    }

    /**
     * Changes scene to the add scene for adding names to the database
     */
    public static void changeSceneAddDBRecordingsMenu () {
        AddDBRecordingsViewController controller = addDBRecordingsLoader.getController();
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_addDBRecordingsMenu);
    }
    
    /**
     * Changes scene to the confrim menu for adding names to the database
     */
    public static void changeSceneConfrimDBRecordingsMenu () {
        DBRecordingConfirmView controller = confirmDBRecordingsLoader.getController();
        controller.setNameLabel(AddDBRecordingsViewController.getCurrentDBName());
        PlayRecordings.stopPlayRecording();
        _primaryStage.setScene(_confirmDBRecordingsMenu);
    }
}