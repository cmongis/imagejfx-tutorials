/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5;

import javafx.concurrent.Task;

/**
 *
 * @author cyril
 */
public class TaskSubmittedEvent extends TaskEvent{
    
    public TaskSubmittedEvent(Task task) {
        super(task);
    }
    
}
