package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import javax.sound.sampled.*;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MicTestController extends SideButtons implements Initializable {

	private SwingWorker<Void,Void> _micTestWorker;

	@FXML
	private ProgressBar _micVolume;

	@FXML
	private Button backBtn;


	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Swing worker used so that the application would be responsive
		_micTestWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				TargetDataLine line = null;
				AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
				if (!AudioSystem.isLineSupported(info)) {
					
				}
				// Obtain and open the line.
				try {
					line = (TargetDataLine) AudioSystem.getLine(info);
					line.open(format);
					line.start();
				} catch (LineUnavailableException ex) {
					
				}


				while(true) {
					byte[] bytes = new byte[line.getBufferSize() / 5];
					line.read(bytes, 0, bytes.length);
					double output = (double) calculateRMSLevel(bytes);
					_micVolume.setProgress((double)output / 100);
				}
			}
		};
		_micTestWorker.execute();
	}

	/**
	 * helper method that calcalutes the RMS level when inputted audio data
	 * @param audioData
	 * @return
	 */
	public int calculateRMSLevel(byte[] audioData) {
		long lSum = 0;
		for(int i=0; i<audioData.length; i++)
			lSum = lSum + audioData[i];

		double dAvg = lSum / audioData.length;

		double sumMeanSquare = 0d;
		for(int j=0; j<audioData.length; j++)
			sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

		double averageMeanSquare = sumMeanSquare / audioData.length;
		return (int)(Math.pow(averageMeanSquare,0.5d) + 0.5);
	}

	/**
	 * Callback function thats called when the back button is clicked, it cancels the background thread and 
	 * changes scene
	 */
	@FXML
	public void handleBackButton() {
		_micTestWorker.cancel(true);
		Main.changeSceneRecord();
	}

}
