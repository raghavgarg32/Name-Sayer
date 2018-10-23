package helpers;

import javafx.concurrent.Task;
import java.util.List;

/**
 * Runs bash commands on the swing worker so the UI doesn't crash
 */
public class BashCommandWorker extends Task<List<Integer>> {
    private String command;

    /**
     * Gets the command, prepares it for being executed and then executes it
     * @param basCommand
     */
    public BashCommandWorker(String basCommand) {
        command = basCommand;
        new Thread(this).start();
    }

    /**
     * Runs the bash command process in the worker thread
     */

    @Override
    protected List<Integer> call() throws Exception {
        ProcessBuilder commandProcessBuilder = new ProcessBuilder("/bin/sh", "-c", command);
        Process commandProcess = commandProcessBuilder.start();

        return null;
    }
}