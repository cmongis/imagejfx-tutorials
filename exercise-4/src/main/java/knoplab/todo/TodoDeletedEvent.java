/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

/**
 *
 * @author cyril
 */
public class TodoDeletedEvent extends TodoEvent {
    
    public TodoDeletedEvent(TodoTask task) {
        super(task);
    }
    
}
