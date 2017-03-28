/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import java.util.List;
import org.scijava.plugin.SciJavaPlugin;

/**
 *
 * @author cyril
 */
public interface TodoView extends SciJavaPlugin {

    void init();

    List<TodoTask> getSelectedTask();

    void show();

    void refresh();

    void select(List<TodoTask> taskList);

    void setSelection(TodoTask task, boolean selection);

    boolean isSelected(TodoTask task);
    
}
