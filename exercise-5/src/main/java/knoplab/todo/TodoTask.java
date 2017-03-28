/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

import javafx.beans.property.BooleanProperty;

/**
 *
 * @author cyril
 */
public interface TodoTask {

    public String getText();

    public boolean isDone();

    public void setDone(boolean isDone);

    public void setText(String text);

    public TaskPriority getPriority();
}
