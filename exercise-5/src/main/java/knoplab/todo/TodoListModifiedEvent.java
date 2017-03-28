/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import org.scijava.event.SciJavaEvent;

/**
 *
 * @author cyril
 */
public class TodoListModifiedEvent extends TodoEvent{
    
    public TodoListModifiedEvent(TodoTask task) {
        super(task);
    }
    
}
