package namesayer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class RewardMenuController implements Initializable {
	
	private static int points;
	
	
	@FXML
	private Button tenPointBtn;
	
	@FXML
	private Button twentyPointBtn;
	
	@FXML
	private Button thirtyPointBtn;
			
	
	@FXML
	public void handle10PointButton() {
		
	}
	
	@FXML
	public void handle20PointButton() {
		
	}
	
	@FXML
	public void handle30PointButton() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if(points < 10 ) {
			tenPointBtn.setVisible(false);
			twentyPointBtn.setVisible(false);
			thirtyPointBtn.setVisible(false);
		}
	}
	
	public void increaseRewardPoint() {
		
	}
}
