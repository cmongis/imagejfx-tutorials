/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author cyril
 */
public interface TodoService {
    
    public List<TodoTask> getTaskList();
    
    public void addTask(String task);
    
    public void addTask(TodoTask task);
    
    public void addTaskListener(Consumer<TodoEvent> event);
    
    public void removeTask(TodoTask task);
    
    
    
}
