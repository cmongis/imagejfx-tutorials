/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.exercise6;

import de.knoplab.md5.ui.MD5CheckUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scijava.SciJava;

/**
 *
 * @author cyril
 */
public class App extends Application {
    
    
    public static void main(String... args) {
        launch(args);
    }
    
     @Override
    public void start(Stage primaryStage) throws Exception {

        MD5CheckUI controller = new MD5CheckUI();
        
        SciJava scijava = new SciJava();
        scijava.context().inject(controller);
        
        
        // creating the scene
        Scene scene = new Scene(controller.getRoot());

        // setting the scene to the stage
        primaryStage.setScene(scene);

        // showing the stage
        primaryStage.show();

    }
}
