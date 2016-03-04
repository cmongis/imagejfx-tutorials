/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knoplab.todo;

/**
 *
 * @author cyril
 */
public class DefaultTask implements TodoTask{

    private String text;
    private boolean isDone;

    public DefaultTask(String text) {
        this.text = text;
    }
    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void setDone(boolean isDone) {
        System.out.println("setting is done");
        this.isDone = isDone;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
    
}
