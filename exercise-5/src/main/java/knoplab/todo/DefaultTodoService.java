/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import knoplab.todo.actions.TodoAction;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;
import org.scijava.service.AbstractService;
import org.scijava.service.SciJavaService;

/**
 *
 * @author cyril
 */
@Plugin(type = SciJavaService.class)
public class DefaultTodoService extends AbstractService implements TodoService {

    private final List<TodoTask> taskList = new ArrayList<>();

    private final List<TodoTask> selectedTaskList = new ArrayList<>();

    private List<TodoAction> actionList;

    @Parameter
    EventService eventService;

    @Parameter
    PluginService pluginService;

    public DefaultTodoService() {
        super();
    }

    // note that we moved the addTask method to the
    // initialize method. This method is usually called
    // after a service has been injected with it dependancies.
    @Override
    public void initialize() {
        addTask("Do something first", TaskPriority.IMPORTANT);
        addTask("Do something afterwards", TaskPriority.NORMAL);
        addTask("Do something later", TaskPriority.TOMORROW);

    }

    @Override
    public List<TodoTask> getTaskList() {
        return taskList;
    }

    @Override
    public void addTask(String task, TaskPriority priority) {
        addTask(new DefaultTask(task, priority));
    }

    @Override
    public void addTask(TodoTask task) {
        taskList.add(task);
        eventService.getContext().inject(task);
        Collections.sort(taskList, this::compareTask);

        if (eventService != null) {
            eventService.publishLater(new TodoAddedEvent(task));
        }
    }

    private int compareTask(TodoTask t1, TodoTask t2) {
        return t1.getPriority().compareTo(t2.getPriority());
    }

    @Override
    public void removeTask(TodoTask task) {

        taskList.remove(task);
        eventService.publishLater(new TodoDeletedEvent(task));
    }

    @Override
    public void execute(TodoAction action) {
        
       
        
        action.run();
    }

    @Override
    public List<TodoAction> getActionList() {
        if (actionList == null) {
            actionList = pluginService.createInstancesOfType(TodoAction.class);

        }
        return actionList;
    }

}
