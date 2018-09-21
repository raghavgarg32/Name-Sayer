package namesayer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for the recording scene
 */
public class RecordView {
	private Service<Void> _backgroundThread;
	
	private static Timer progressTimer = new Timer();
	
	
	@FXML
	private Button micTestBtn;
	
	@FXML
	private Button backBtn;
	
	@FXML
	private ProgressBar recordBar;


    @FXML
    private Label recordLabel;
    
    
    @FXML
    public void handleMicTestButton() {
    	Main.changeSceneMicTest();
    }
    

    @FXML
    public void handleRecordButton() throws IOException {
    	progressTimer.cancel();
		recordBar.setProgress(0.0);
        recordLabel.setText("Recording now...");
      
        
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				double progress = recordBar.getProgress();
				if (progress == 1) {
					recordBar.setProgress(0);
					progressTimer.cancel();
					return;
				}
				
				recordBar.setProgress(progress + 0.01);
			}	
		};
		
		progressTimer = new Timer();
		progressTimer.scheduleAtFixedRate(timerTask, 0, 50);
		
		
		//Use a background thread for recording 
				_backgroundThread = new Service<Void>() {
					@Override
					protected Task<Void> createTask() {
						return new Task<Void>() {
							@Override
							protected Void call() throws Exception {
								ProcessBuilder recordBuilder = new ProcessBuilder("ffmpeg","-y","-f","alsa","-ac","1"
										,"-ar","44100","-i","default","-t", "5","tempAudio.wav");
								try {
									Process p = recordBuilder.start();
									p.waitFor();
									
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 
								return null;
							}
						};
					}
				};

				_backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					//Change scene when recording is finished
					@Override
					public void handle(WorkerStateEvent event) {
						recordingEnd();
						Main.changeScenePractice();

					}		
				});
				_backgroundThread.start();
			}

    @FXML
    public void handleBackBtn() {
    	if(_backgroundThread == null) {
    		recordLabel.setText("Press record to have your voice recorded");
			Main.changeScenePractice();
			return;
		}
		//Check if a background thread is running
		if(_backgroundThread.isRunning()) {
			progressTimer.cancel();
			recordBar.setProgress(0.0);
			_backgroundThread.cancel();
		}
		recordLabel.setText("Press record to have your voice recorded");
		Main.changeScenePractice();
	}
    
    
    public void recordingEnd(){
        recordLabel.setText("Record DONE");
    }
    
    
   
   
    
}
