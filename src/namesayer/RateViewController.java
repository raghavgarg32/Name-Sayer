package namesayer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Sets up the rating system for the database recordings
 */
public class RateViewController implements Initializable  {

	/**
	 * This method is require to be implemented
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleBadButton() {
		String selectionName = PracticeMenuController.getCurrentName();
		String path = "./Database/"+selectionName+"/Ratings/userReview.txt";
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("The recording is of bad quality");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			Main.changeScenePractice();
		}

	}

	/**
	 * If the user gives the database recording a bad review this will output it to a text file
	 */
	@FXML
	public void handleGoodButton() {
		String selectionName = PracticeMenuController.getCurrentName();
		String path = "./Database/"+selectionName+"/Ratings/userReview.txt";
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("The recording is of good quality");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			Main.changeScenePractice();
		}
	}
	
	@FXML
	public void handleBackButton() {
		Main.changeScenePractice();
	}

}
