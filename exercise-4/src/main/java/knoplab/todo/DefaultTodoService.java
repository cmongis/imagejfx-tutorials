/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.ArrayList;
import java.util.List;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.SciJavaService;
import org.scijava.service.Service;

/**
 *
 * @author cyril
 */
@Plugin(type = SciJavaService.class)
public class DefaultTodoService extends AbstractService implements TodoService {

    private final List<TodoTask> taskList = new ArrayList<>();
   

    @Parameter
    EventService eventService;
    
    
    public DefaultTodoService() {
        super();
  
    }
    
    // note that we moved the addTask method to the
    // initialize method. This method is usually called
    // after a service has been injected with it dependancies.
    @Override
    public void initialize() {
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
        if(eventService != null)
        eventService.publishLater(new TodoAddedEvent(task));
    }

   

    @Override
    public void removeTask(TodoTask task) {
        System.out.println("Removing task");
        taskList.remove(task);
        eventService.publishLater(new TodoDeletedEvent(task));
    }

    

}
