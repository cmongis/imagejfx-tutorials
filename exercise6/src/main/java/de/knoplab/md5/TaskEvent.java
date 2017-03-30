/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5;

import javafx.concurrent.Task;
import org.scijava.event.SciJavaEvent;

/**
 *
 * @author cyril
 */
public class TaskEvent extends SciJavaEvent{
    final Task task;

    public TaskEvent(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
    
    
    
}
