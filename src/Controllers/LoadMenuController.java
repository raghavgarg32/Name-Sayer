package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;


    public class LoadMenuController  {

        @FXML
        private ProgressBar loadingBar;

        /**
         * This method will start the processes needed for the rest of the application to function, once the method has finished said processes
         * the scene will change
         */
        public void load() {
            System.out.println("started");
            loadingBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

            Service<Void> backgroundThread = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            ProcessBuilder settingUpDatabaseWorker = new ProcessBuilder("/bin/bash","-c",
                                    "mkdir User-Recordings;\n"
                                            + "mkdir Concat-Recordings;\n"
                                            + "cd Database;\n"
                                            + "rename 'y/A-Z/a-z/' *\n"
                                            + "\n" + "for i in $(ls); do\n"
                                            + "\n"
                                            + "names=$(echo $i | awk -F\"_\" '{print $NF}')\n"
                                            + "nameWithourExtension=\"${names%.*}\"\n"
                                            + "    echo \"This is i $i \"\n"
                                            + "    mkdir \"$nameWithourExtension\"\n"
                                            + "    \n"
                                            + "    cd \"$nameWithourExtension\"\n"
                                            + "    \n"
                                            + "    mkdir \"Database-Recordings\"\n"
                                            + "    \n"
                                            + "    mkdir \"User-Recordings\"\n"
                                            + "\n"
                                            + "    mkdir \"Ratings\"\n"
                                            + "    \n"
                                            + "    mv \"../$i\" \"./Database-Recordings\"\n"
                                            + "\n"
                                            + "    cd ..\n"
                                            + "\n"
                                            + "done\n"
                                            + "exit 1 \n");
                            Process loadingProcess;
                            try {
                                loadingProcess = settingUpDatabaseWorker.start();
                                loadingProcess.waitFor();
                                System.out.println("done");
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                }
            };
            backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent event) {
                    DataBaseController controller = new DataBaseController();
                    controller.gettingRecordings();
                    Main.changeSceneDataBase();
                }

            });
            backgroundThread.start();
        }
    }
