/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import static jdk.nashorn.internal.objects.NativeJava.type;
import org.scijava.event.SciJavaEvent;

/**
 *  Event specifying a type and the concerned task
 * @author cyril
 */
public class TodoEvent extends SciJavaEvent{
    private final TodoTask task;
    
    public TodoEvent(TodoTask task) {
        this.task = task;
    }
    
    public TodoTask getTask() {
        return task;
    }
    
    
    
    
}
