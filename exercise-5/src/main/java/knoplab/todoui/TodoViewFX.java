/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todoui;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import knoplab.todo.TodoTask;
import knoplab.todo.TodoView;
import org.scijava.Context;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * @author cyril
 */
@Plugin(type = TodoView.class)
public class TodoViewFX extends Application implements TodoView {

    // the pane is private because... damn JavaFX
    static private TodoPane pane;

    // also has to be static because damn JavaFX
    @Parameter
    static private Context context;
    
    
    public TodoViewFX(){
      
       
    }
    
    @Override
    public void init() {
        
        // method left blank, not really useful
       
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // creating our pane and injecting things
        pane = new TodoPane();

        
        context.inject(pane);
        
        
        pane.init();
        
        // creating the scene
        Scene scene = new Scene(pane);

        // setting the scene to the stage
        primaryStage.setScene(scene);

        // showing the stage
        primaryStage.show();

    }

    @Override
    public List<TodoTask> getSelectedTask() {
        return pane.getSelectedTask();
    }

    @Override
    public void show() {
        launch();
    }

    @Override
    public void refresh() {
        // refresh the pane
        pane.refresh();
    }

    @Override
    public void select(List<TodoTask> taskList) {
        // ask the pane to select this list of task
        pane.select(taskList);
    }

    @Override
    public void setSelection(TodoTask task, boolean selection) {
        pane.setSelection(task, selection);
    }

    @Override
    public boolean isSelected(TodoTask task) {
        return pane.isSelected(task);
    }
}
