/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import javafx.concurrent.Task;

/**
 *  Event specifying a type and the concerned task
 * @author cyril
 */
public class TodoEvent {
    private final TodoEventType type;
    private final TodoTask task;
    
    public TodoEvent(TodoEventType type,TodoTask task) {
        this.type = type;
        this.task = task;
    }

    public TodoEventType getType() {
        return type;
    }

    public TodoTask getTask() {
        return task;
    }
    
    
    
    
}
