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

    public DefaultTodoService() {
        
        
        addTask("Do something first");
        addTask("Do something afterwards");
        
    }
    
    @Override
    public List<TodoTask> getTaskList() {
        return taskList;
    }
    
    @Override
    public void addTask(String task) {
        addTask(new DefaultTask(task));
    }

    @Override
    public void addTask(TodoTask task) {
        System.out.println("Adding task");
        taskList.add(task);
        fireEvent(new TodoEvent(TodoEventType.TASK_ADDED, task));
    }

    @Override
    public void addTaskListener(Consumer<TodoEvent> listener) {
        listenerList.add(listener);

    }

    @Override
    public void removeTask(TodoTask task) {
        System.out.println("Removing task");
        taskList.remove(task);
        fireEvent(new TodoEvent(TodoEventType.TASK_DELETED, task));
    }

    private void fireEvent(TodoEvent event) {
        System.out.println("Contacting listeners");
        for (Consumer<TodoEvent> listener : listenerList) {
            listener.accept(event);
        }
    }

}
