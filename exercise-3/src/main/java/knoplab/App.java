package knoplab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import knoplab.todoui.TodoUi;

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
        
        // we create the controller/javafx element
        TodoUi ui = new TodoUi();
        
        // creating the scene
        Scene scene = new Scene(ui);
        
        // setting the scene to the stage
        primaryStage.setScene(scene);
        
        // showing the stage
        primaryStage.show();
        
    }
}
