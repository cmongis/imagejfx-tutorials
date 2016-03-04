/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author cyril
 */
public class DefaultTodoService implements TodoService {

    
    private final List<TodoTask> taskList = new ArrayList<>();
    private final List<Consumer<TodoEvent>> listenerList = new ArrayList<>();
    
    
    public List<TodoTask> getTaskList() {
        return taskList;
    }

    public void addTask(String task) {
        addTask(new DefaultTask(task));        
    }

    public void addTask(TodoTask task) {
        
        taskList.add(task);
        fireEvent(new TodoEvent(TodoEventType.TASK_ADDED, task));
    }

    public void addTaskListener(Consumer<TodoEvent> listener) {
        listenerList.add(listener);
       
    }

    public void removeTask(TodoTask task) {
        taskList.remove(task);
         fireEvent(new TodoEvent(TodoEventType.TASK_DELETED, task));
    }
    
    private void fireEvent(TodoEvent event) {
        System.out.println("Contacting listeners");
        for(Consumer<TodoEvent> listener : listenerList) {
            listener.accept(event);
        }
    }
    
}
