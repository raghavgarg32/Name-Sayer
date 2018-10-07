package namesayer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelpMenuController implements Initializable {

	@FXML
	private ImageView tabMenuPicture,dataBasePic,practiceMenuPic,rateMenuPic,recordMenuPic,confirmMenuPic,rewardMenuPic;
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image sideButtons = new Image("/HelpPictures/MenuTab.png");
		Image dataBaseMenu = new Image("/HelpPictures/Database.png");
		Image practiceMenu = new Image("/HelpPictures/PracticeMenu.png");
		Image rateMenu = new Image("/HelpPictures/RateMenu.png");
		Image recordMenu = new Image("/HelpPictures/RecordMenu.png");
		Image confirmMenu = new Image("/HelpPictures/ConfirmMenu.png");
		Image rewardMenu = new Image("/HelpPictures/RewardMenu.png");
		
		
		tabMenuPicture.setImage(sideButtons);
		dataBasePic.setImage(dataBaseMenu);
		practiceMenuPic.setImage(practiceMenu);
		rateMenuPic.setImage(rateMenu);
		recordMenuPic.setImage(recordMenu);
		confirmMenuPic.setImage(confirmMenu);
		rewardMenuPic.setImage(rewardMenu);
	}
	
	
	
}
