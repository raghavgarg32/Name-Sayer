package helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
            System.out.println(filename);

            command = command + " -i " + file.getPath();
        }

        command = command + " -filter_complex '[0:0][1:0]concat=n=" + listOfFiles.size()
                + ":v=0:a=1[out]' -map '[out]' " + name + ".wav";

        System.out.println(command);

        ProcessBuilder concatBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process concatProcess = concatBuilder.start();
            concatProcess.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeSilence(new File("Concat-Recordings/" + name + ".wav"));

        normalizeAudio(new File("Concat-Recordings/" + name + ".wav"));

        return new File(name + ".wav");
    }

    public static void removeSilence(File file) {
        ProcessBuilder whitenoiseBuilder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -hide_banner -i "
                + file.getPath() + " -af " + "silenceremove=1:0:-50dB:1:5:-50dB:0 " + file.getPath());

        try {
            Process whitenoiseProcess = whitenoiseBuilder.start();
            whitenoiseProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void normalizeAudio(File file) {
        ProcessBuilder normalizeBuilder = new ProcessBuilder("/bin/bash", "-c",
                "ffmpeg -y -i " + file.getPath() + " -af dynaudnorm " + file.getPath());
        Process normalize;
        try {
            normalize = normalizeBuilder.start();
            normalize.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
