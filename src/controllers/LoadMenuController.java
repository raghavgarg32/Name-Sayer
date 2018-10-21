package controllers;

import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;


    public class LoadMenuController  {

        @FXML
        private ProgressBar loadingBar;

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
                                            + "nameWithoutExtension=\"${names%.*}\"\n"
                                            + "    echo \"This is i $i \"\n"
                                            + "    mkdir \"$nameWithoutExtension\"\n"
                                            + "    \n"
                                            + "    cd \"$nameWithoutExtension\"\n"
                                            + "    \n"
                                            + "    mkdir \"Database-Recordings\"\n"
                                            + "    \n"
                                            + "\n"
                                            + "    touch \"Ratings.txt\"\n"
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
//					TimeUnit.SECONDS.sleep(3);
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
                    HomeViewController controller = new HomeViewController();
                    controller.gettingRecordings();
                    Main.changeSceneDataBase();
                }

            });
            backgroundThread.start();
        }
    }
