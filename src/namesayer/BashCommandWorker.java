package namesayer;

import javax.swing.SwingWorker;

/**
 * Runs bash commands on the swing worker so the UI doesn't crash
 */
public class BashCommandWorker extends SwingWorker<Void, Void> {
    private String command;

    /**
     * Gets the command, prepares it for being executed and then executes it
     * @param basCommand
     */
    public BashCommandWorker(String basCommand) {
        command = basCommand;
        this.execute();
    }

    /**
     * Runs the bash command process in the worker thread
     */
    @Override
    protected Void doInBackground() throws Exception {
        ProcessBuilder commandProcessBuilder = new ProcessBuilder("/bin/sh", "-c", command);
        Process commandProcess = commandProcessBuilder.start();

        return null;
    }
}
