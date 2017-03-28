/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.List;
import org.scijava.service.Service;

/**
 *
 * @author cyril
 */
public interface TodoViewService extends Service{
    
    
    void showView();
    
    List<TodoTask> getSelectedTasks();
    
    void select(List<TodoTask> task);
    
    boolean isSelected(TodoTask task);
    
    void setSelected(TodoTask task, boolean selection);
    
}
