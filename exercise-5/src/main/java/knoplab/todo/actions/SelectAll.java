/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo.actions;

import knoplab.todo.TodoService;
import knoplab.todo.TodoViewService;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * @author cyril
 */
@Plugin(type = TodoAction.class, label="Select all")
public class SelectAll implements TodoAction{
    
    @Parameter
    TodoViewService todoViewService;

    @Parameter
    TodoService todoService;
    
    @Parameter
    EventService eventService;
    
    @Override
    public void run() {
        todoViewService.select(todoService.getTaskList());
               
       
        
    }
    
    
    
    
    
    
    
}
