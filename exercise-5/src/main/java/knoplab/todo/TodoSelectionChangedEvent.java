/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import javafx.scene.chart.PieChart;
import org.scijava.event.SciJavaEvent;

/**
 *
 * @author cyril
 */
public class TodoSelectionChangedEvent extends SciJavaEvent {

    
    final TodoTask task;
    final boolean selected;
    public TodoSelectionChangedEvent(TodoTask task, boolean selection) {
        super();
        
        this.task = task;
        this.selected = selection;
    }

    public TodoTask getTask() {
        return task;
    }

    public boolean isSelected() {
        return selected;
    }
    
    
    
    
    
    
    
    
}
