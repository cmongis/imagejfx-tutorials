/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todoui;

import javafx.beans.property.Property;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import knoplab.todo.TodoTask;

/**
 *
 * @author cyril
 */
public class TodoTaskWrapper implements TodoTask {

    private final TodoTask task;

    private Property<String> textProperty;
    private Property<Boolean> doneProperty;

    public TodoTaskWrapper(TodoTask task) {

        this.task = task;

        // the BeanPropertyBuilder creates a property that calls the getters and setters of the task object;
        // In other words, each time the property is changed, it will called setDone or getIsDone from
        // the wrapped task.
        try {
            this.doneProperty = new JavaBeanObjectPropertyBuilder<>().bean(task).name("done").build();
            this.textProperty = new JavaBeanStringPropertyBuilder().bean(task).name("text").build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getText() {
        return task.getText();
    }

    @Override
    public boolean isDone() {
        return task.isDone();
    }

    @Override
    public void setDone(boolean isDone) {
        // When setting the property, it will non only 
        // change the wrapped task, but it will
        // also automatically notify all the listeners
        // of the "done" property.
        doneProperty.setValue(isDone);
    }

    @Override
    public void setText(String text) {
        // same as before
        textProperty.setValue(text);
    }

    public TodoTask getTask() {
        return task;
    }

    public Property<String> textProperty() {
        return textProperty;
    }

    public Property<Boolean> doneProperty() {
        return doneProperty;
    }
}
