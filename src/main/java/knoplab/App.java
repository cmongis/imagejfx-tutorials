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
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        TodoUi ui = new TodoUi();
        
        Scene scene = new Scene(ui);
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
    }
}
