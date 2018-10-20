package namesayer;

import javax.sound.sampled.*;
import javax.swing.*;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;

public class PlayRecordings {
    private static Task<Void> _playWorker;
    private static Thread playThread;

    public static void handlingPlayingRecordings(String ...pathToFile){

   
    	
    	
        _playWorker = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                AudioInputStream stream;
                AudioFormat format;
                DataLine.Info info;
                SourceDataLine sourceLine;

                for (String s : pathToFile) {

                    try {
                        stream = AudioSystem.getAudioInputStream(new File(s));
                        format = stream.getFormat();

                        info = new DataLine.Info(SourceDataLine.class, format);
                        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                        sourceLine.open(format);

                        sourceLine.start();

                        int nBytesRead = 0;
                        int BUFFER_SIZE = 128000;
                        byte[] abData = new byte[BUFFER_SIZE];
                        while (nBytesRead != -1) {
                            try {
                                nBytesRead = stream.read(abData, 0, abData.length);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (nBytesRead >= 0) {
                                @SuppressWarnings("unused")
                                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                            }
                        }

                        sourceLine.drain();
                        sourceLine.close();

                    } catch (Exception e) {

                    }
                }
                return null;
            }

        };
        
        playThread = new Thread(_playWorker);
        playThread.start();
    }
    
    public static void stopPlayRecording() {
    	if(_playWorker != null) {
        	_playWorker.cancel(true);
        	playThread.interrupt();
    	}
    }
}
