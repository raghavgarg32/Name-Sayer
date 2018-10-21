package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import helpers.SideButtons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelpMenuController extends SideButtons implements Initializable {

	@FXML
	private ImageView tabMenuPicture,dataBasePic,practiceMenuPic,rateMenuPic,
			recordMenuPic,confirmMenuPic,rewardMenuPic,userRecordingPic,databaseRecordingPic;




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image sideButtons = new Image("/helpPictures/MenuTab.png");
		Image dataBaseMenu = new Image("/helpPictures/Database.png");
		Image practiceMenu = new Image("/helpPictures/PracticeMenu.png");
		Image rateMenu = new Image("/helpPictures/RateMenu.png");
		Image recordMenu = new Image("/helpPictures/RecordMenu.png");
		Image confirmMenu = new Image("/helpPictures/ConfirmMenu.png");
		Image rewardMenu = new Image("/helpPictures/RewardMenu.png");
		Image userRecordingMenu = new Image("/helpPictures/userRecording.png");
		Image databaseRecordingMenu = new Image("/helpPictures/databaseRecordings.png");


		tabMenuPicture.setImage(sideButtons);
		dataBasePic.setImage(dataBaseMenu);
		practiceMenuPic.setImage(practiceMenu);
		rateMenuPic.setImage(rateMenu);
		recordMenuPic.setImage(recordMenu);
		confirmMenuPic.setImage(confirmMenu);
		rewardMenuPic.setImage(rewardMenu);
		userRecordingPic.setImage(userRecordingMenu);
		databaseRecordingPic.setImage(databaseRecordingMenu);
	}



}