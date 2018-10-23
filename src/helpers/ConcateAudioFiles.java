package helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is a helper class that concatenates multiple audio files if the name has multiple names and process them,
 * remove silence and normalize them
 */
public class ConcateAudioFiles {
    /**
     * Method will take in a list of .wav files and concatenat them into one .wav
     * file and return it
     *
     * @param listOfFiles
     * @return concat audio file
     */
    public static File createConcatFile(List<File> listOfFiles, String name) {

        String command = "ffmpeg -y";

        for (File file : listOfFiles) {
            String filename = file.getPath().substring(0, file.getPath().lastIndexOf('.'));
            
            //add all the files that need to be concatenated into the ffmpeg command
            command = command + " -i " + file.getPath();
        }

        command = command + " -filter_complex '[0:0][1:0]concat=n=" + listOfFiles.size()
                + ":v=0:a=1[out]' -map '[out]' " + name + ".wav";

        

        ProcessBuilder concatBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process concatProcess = concatBuilder.start();
            concatProcess.waitFor();
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        removeSilence(new File("Concat-Recordings/" + name + ".wav"));

        normalizeAudio(new File("Concat-Recordings/" + name + ".wav"));

        return new File(name + ".wav");
    }

    /**
     * Removes the silence from a specified audio file
     * @param file
     */
    public static void removeSilence(File file) {
        ProcessBuilder whitenoiseBuilder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -hide_banner -i "
                + file.getPath() + " -af " + "silenceremove=1:0:-50dB:1:5:-50dB:0 " + file.getPath());

        try {
            Process whitenoiseProcess = whitenoiseBuilder.start();
            whitenoiseProcess.waitFor();
        } catch (InterruptedException e) {

        } catch (IOException e) {

        }
    }

    /**
     * Normalizes the audio of a specified audio file
     * @param file
     */
    public static void normalizeAudio(File file) {
        ProcessBuilder normalizeBuilder = new ProcessBuilder("/bin/bash", "-c",
                "ffmpeg -y -i " + file.getPath() + " -af dynaudnorm " + file.getPath());
        Process normalize;
        try {
            normalize = normalizeBuilder.start();
            normalize.waitFor();
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }

    }
}
