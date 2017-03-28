package knoplab;

import knoplab.todo.TodoViewService;
import org.scijava.SciJava;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

/**
 * Hello world!
 *
 */
public class App {

  

    public App() throws Exception {
        SciJava scijava = new SciJava();
        scijava.getContext().inject(this);
        scijava.get(TodoViewService.class).showView();
        
    }

    public static void main(String[] args) throws Exception {
        new App();

    }

}
