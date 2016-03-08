package knoplab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.naming.Context;
import knoplab.todoui.TodoUi;
import org.scijava.SciJava;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args )
    {
        // Java FX way to launch an app
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        SciJava scijava = new SciJava();
        
        // we create the controller/javafx element
        TodoUi ui = new TodoUi();
        
        // executing the SciJava injection (Services and EventHandling) on the Ui
        scijava.context().inject(ui);
        
        // we created a method which is called after the injection.
        // Indeed, before, the injection, all services are
        // pointing to Null.
        ui.afterInjection();
        
        
        // creating the scene
        Scene scene = new Scene(ui);
        
        // setting the scene to the stage
        primaryStage.setScene(scene);
        
        // showing the stage
        primaryStage.show();
        
    }
}
