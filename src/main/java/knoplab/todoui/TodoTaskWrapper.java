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
        // any change of done will notify
        // the property listener
        doneProperty.setValue(isDone);
    }

    @Override
    public void setText(String text) {
        // any change of text will notify
        // the property listeners
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
