/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.List;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 *
 * @author cyril
 */
@Plugin(type = Service.class)
public class DefaultTodoViewService extends AbstractService implements TodoViewService {

    @Parameter
    PluginService pluginService;

    @Parameter
    EventService eventService;

    TodoView currentView;

     List<TodoView> viewList;

    
    
    @Override
    public void showView() {

        if(viewList == null) {
            viewList = pluginService.createInstancesOfType(TodoView.class);
            if (viewList.size() == 0) {
                System.out.println("No view found");
            } else {
                TodoView view = viewList.get(0);
                currentView = view;


                view.init();
                view.show();

            }
        }

    }

    @Override
    public List<TodoTask> getSelectedTasks() {

        return currentView.getSelectedTask();
    }

    @Override
    public void setSelected(TodoTask task, boolean selected) {
        currentView.setSelection(task, selected);
        // currentView.setSelection(task, selected);
        eventService.publish(new TodoSelectionChangedEvent(task, selected));

    }

    @Override
    public boolean isSelected(TodoTask task) {
        return currentView.isSelected(task);
    }

    @Override
    public void select(List<TodoTask> task) {
        currentView.select(task);
    }

}
