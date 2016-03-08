/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.List;
import org.scijava.service.SciJavaService;

/**
 *
 * @author cyril
 */
public interface TodoService extends SciJavaService{
    
    public List<TodoTask> getTaskList();
    
    public void addTask(String task);
    
    public void addTask(TodoTask task);
    
   
    
    public void removeTask(TodoTask task);
    
    
    
}
