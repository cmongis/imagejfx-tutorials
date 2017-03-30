/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.concurrent.Task;
import org.apache.commons.codec.digest.DigestUtils;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.SciJavaService;

/**
 *
 * @author cyril
 */
@Plugin(type = SciJavaService.class)
public class DefaultMD5Service extends AbstractService implements MD5Service {

    @Parameter
    private EventService eventService;

    /**
     * Current task
     */
    private Task lastTask = null;

    /**
     * Folder to analyse
     */
    private File folder;

    /**
     * Folder to process
     *
     * @param fodler to process
     */
    public void setCurrentFolder(File folder) {

        if (folder.isDirectory() == false) {
            throw new IllegalArgumentException("The input must be a folder !");
        } else {
            this.folder = folder;
            eventService.publish(new FolderChangedEvent(folder));
        }

    }

    @Override
    public File getCurrentFolder() {
       return folder;
    }

    @Override
    public Task getCurrentTask() {
       return lastTask;
    }


    @Override
    public Task<Boolean> startMD5Processing() {
        lastTask = new SumTask(getCurrentFolder());
        new Thread(lastTask).start();
        return lastTask;

    }

    /**
     * This function execute the MD5 of a single file. It will raise an
     * exception if the input is not a file.
     *
     * @param file directory
     * @throws IOException
     */
    protected void md5file(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Only files can be check summed !");
        }

        // calculating the output
        byte[] md5 = DigestUtils.md5(new FileInputStream(file));

        // getting the output file
        File outputFile = new File(file.getAbsolutePath() + ".md5");
        Files.write(Paths.get(outputFile.getAbsolutePath()), md5);

    }

    @Override
    public void cancelMD5Processing() {
        lastTask.cancel();
    }

    /**
     * This class execute the MD5 sum of each file of the input folder
     */
    private class SumTask extends Task<Boolean> {

        final File directory;

        private static final String MESSAGE = "Processing %s (%d/%d)...";

        public SumTask(File dir) {
            this.directory = dir;
        }

        public Boolean call() throws Exception {

            // publishing the beginning of the action
            eventService.publish(new TaskSubmittedEvent(this));

            // if it's a directory, the task should return false
            if (!directory.isDirectory()) {
                return false;
            }

            // filtering the files contained by the directory
            List<File> toProcess = Stream
                    .of(directory.listFiles())
                    .filter(File::isFile) // only takes files,
                    .filter(file -> file.isHidden() == false) // which are not hidden
                    .filter(file -> file.getName().endsWith(".md5") == false) // and don't finish with .md5
                    .collect(Collectors.toList());

            // count the total number of files to process
            int total = toProcess.size();

            // we use a goold old loop since we need a count
            for (int i = 0; i != total; i++) {
                Thread.sleep(1000);

                // if the user wanted to cancel the task
                if (isCancelled()) {
                    return false;
                }
                File file = toProcess.get(i);
                updateMessage(String.format(MESSAGE, file.getName(), i, total));
                updateProgress(i, total);
                md5file(file);

            }

            return true;
        }

        // throw events ended when the task is over
        @Override
        protected void succeeded() {
            eventService.publish(new TaskEndedEvent(this));

        }

        @Override
        protected void cancelled() {
            eventService.publish(new TaskEndedEvent(this));
        }

        @Override
        protected void failed() {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error when summing...", getException());
            eventService.publish(new TaskEndedEvent(this));
        }

    }

}
