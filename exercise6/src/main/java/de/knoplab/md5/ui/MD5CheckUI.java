/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5.ui;

import de.knoplab.md5.FolderChangedEvent;
import de.knoplab.md5.MD5Service;
import de.knoplab.md5.TaskEndedEvent;
import de.knoplab.md5.TaskSubmittedEvent;
import java.io.File;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import org.scijava.event.EventHandler;
import org.scijava.plugin.Parameter;

/**
 *
 * @author cyril
 */
public class MD5CheckUI {

    @FXML
    Button fileButton;
    @FXML
    Button startButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    Label progressLabel;

    /**
     * Property holding the directory to process.
     */
    private final ObjectProperty<File> fileProperty = new SimpleObjectProperty<>();

    /**
     * Property holding the current task. Null if not task is executed
     */
    private final ObjectProperty<Task> taskProperty = new SimpleObjectProperty<>();

    /**
     * Binding representing the text of the file button
     */
    private final ObservableValue<String> fileButtonText = Bindings.createStringBinding(this::getFileButtonText, fileProperty);

    /**
     * Binding representing the text of the Start/Cancel button
     */
    private final StringBinding startButtonText = Bindings.createStringBinding(this::getStartButtonText, fileProperty, taskProperty);

    
    
    // Some text
    private static String START = "Start";
    private static String CANCEL = "Cancel";
    private static String CHOOSE_DIRECTORY = "Select a directory";
    private static String DONE = "Done !";
    private static String WAITING  = "Waiting for you.";
    
    // service used
    @Parameter
    MD5Service md5Service;

    final Parent root;

    
    public MD5CheckUI() throws Exception {
        
        // Loading FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MD5CheckUI.fxml"));
        
        // Setting this class as the controller
        loader.setController(this);

        // Loading
        loader.load();
        
        // getting the root
        root = loader.getRoot();
        
        // initializing the buttons and listeners
        init();

    }

    public Parent getRoot() {
        return root;
    }

    /**
     *  Initializing the different buttons and properties
     */
    private void init() {

        // taking care of the file button
        fileButton.textProperty().bind(fileButtonText);
        fileButton.setOnAction(this::onFileButtonClicked);

        // taking care of the start/cancel button
        startButton.textProperty().bind(startButtonText);
        
        // the start/cancel button is disabled if the file property is null
        startButton.disableProperty().bind(fileProperty.isNull());
        
        // setting the action on click
        startButton.setOnAction(this::onStartButtonClicked);
       
        // some logic for when a task is there, and when it's not
        // Advantage : we don't have to worry about how the task
        // ended there in the first place. All we care is what
        // happen when a task is currently running
        taskProperty.addListener(this::onTaskChanged);
        
        // setting the default text of the progress label
        progressLabel.setText(WAITING);

        progressBar.visibleProperty().bind(taskProperty.isNotNull());
        
    }
    
    /*
        Listeners
    */

    protected void onTaskChanged(Observable value, Task oldValue, Task task) {
        
        // if a task is submitted
        if (task != null) {
            // we bind the progress bar and the label
            progressBar.progressProperty().bind(task.progressProperty());
            progressLabel.textProperty().bind(task.messageProperty());
        }
        else {
            
            // if not (newValue == null)
            // we unding verything and set the text to waiting
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().setValue(0.0);
            progressLabel.textProperty().unbind();
            progressLabel.setText(WAITING);
        }
    }

    
    protected double getProgressValue() {
        if(taskProperty.getValue() != null) {
            return taskProperty.getValue().getProgress();
        }
        else {
            return 0d;
        }
    }
    
    
    protected void onStartButtonClicked(ActionEvent event) {
        
        // if a task is running
        if (taskProperty.getValue() != null) {
            // we ask the service to cancel it
            md5Service.cancelMD5Processing();
        } else {
            
            // otherwise, we start a new one
            md5Service.startMD5Processing();
        }
    }

    protected void onFileButtonClicked(ActionEvent event) {
        
        DirectoryChooser chooser = new DirectoryChooser();

        File directory = chooser.showDialog(null);

        if (directory != null) {
            md5Service.setCurrentFolder(directory);
        }

    }
    
    /*
        Methods used for text bindings
    */

    protected String getFileButtonText() {

        // if the file property is empty
        if (fileProperty.getValue() == null) {
            return CHOOSE_DIRECTORY;
        } else {
            // returns the name of the file
            return (fileProperty.getValue().getName());
        }

    }

    protected String getStartButtonText() {
        // if the task property hold the current task
        if (taskProperty.getValue() == null) {
            return START;
        } else {
            return CANCEL;
        }
    }

    /*
        Scijava event handlers
    */
    
    @EventHandler
    public void onFolderChanged(FolderChangedEvent event) {
        Platform.runLater(()->{
            fileProperty.setValue(event.getFolder());
        });
    }
    
    
    @EventHandler
    public void onTaskSubmitted(TaskSubmittedEvent event) {

        Platform.runLater(() -> {

            taskProperty.setValue(event.getTask());
            
        });
    }

    @EventHandler
    public void onTaskEnded(TaskEndedEvent event) {
        Platform.runLater(() -> {
            taskProperty.setValue(null);
            
        });
    }

}
