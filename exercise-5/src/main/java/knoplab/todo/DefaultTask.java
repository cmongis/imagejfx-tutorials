/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;

/**
 *
 * @author cyril
 */
public class DefaultTask implements TodoTask {

    private String text;
    private boolean done;

    private boolean selected;

    private final TaskPriority priority;

    @Parameter
    EventService eventService;
    
    
    public DefaultTask(String text, TaskPriority priority) {
        this.text = text;
        this.priority = priority;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void setDone(boolean isDone) {
        
        this.done = isDone;
        eventService.publish(new TodoModifiedEvent(this));
    }

    @Override
    public void setText(String text) {
        this.text = text;
        eventService.publish(new TodoModifiedEvent(this));
    }

    @Override
    public TaskPriority getPriority() {
        return priority;
    }

   

}
