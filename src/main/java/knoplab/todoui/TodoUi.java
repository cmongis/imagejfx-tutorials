package knoplab.todoui;

import java.io.IOException;
import javafx.beans.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import knoplab.todo.DefaultTodoService;
import knoplab.todo.TodoEvent;
import knoplab.todo.TodoEventType;
import knoplab.todo.TodoService;

/**
 *
 *
 * The TodoUi uses a TodoTaskWrapper that provides additional listenable
 * properties.
 *
 * @author Cyril MONGIS
 */
public class TodoUi extends BorderPane {

    // Service containing the model
    TodoService todoService = new DefaultTodoService();

    // It's called a ViewModel.
    // It contains TodoTaskWrappers. 
    // This object basically implement the TodoTask interface
    // and wrap a existing task while adding javafx properties
    // to the object. Properties can be bound by the view
    // and any change of a TodoTaskWrapper will be automatically
    // synced with the view.
    ObservableList<TodoTaskWrapper> taskList = FXCollections.observableArrayList();

    
    @FXML
    TextField textField; // note that it's okay to have such a simple name if your controller only contains one text field

    @FXML
    ListView<TodoTaskWrapper> listView;

    @FXML
    ListView<TodoTaskWrapper> todoTaskWrapper;

    public TodoUi() throws IOException {

        // To sum up, we extends a BorderPane and we inject an FXML
        // inside it.
        // In this way, the Java class become the center point, not the FXML.
        
        // Be careful that the option use fx:root is ticked in the SceneBuilder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/knoplab/todoui/TodoUi.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        
        
        
        
        // We created a Service that emit specific Events of our
        // construction (See knoplab.todo package)
        // We have TodoEvent and Todo Event types.
        // A listener is just an object of type Consumer which
        // is a Functionnal Interface. Since we want the controller
        // to react to these events, we can just pass the pointer
        // of a method emulating the Consumer Interface.
        todoService.addTaskListener(this::onTaskEvent);
        
        // adding the pre-existing tasks
        todoService.getTaskList().forEach(task->taskList.add(new TodoTaskWrapper(task)));
        
        // setting the cell factory
        listView.setCellFactory(this::createCell);

        // setting the items of the view
        listView.setItems(taskList);
        
        // when ENTER is pressed, we want to add the todo task so we listen to every 
        // key released event.
        textField.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyTyped);
        
      
    }

    // our factory is just a method that creates a TodoTaskCell
    private ListCell<TodoTaskWrapper> createCell(ListView<TodoTaskWrapper> listView) {
        return new TodoTaskCell();
    }

    // 
    private void addTask() {

        // the todo service will later notify the UI that a task has been added
        todoService.addTask(textField.getText());
        textField.setText("");

    }

    /*
        Events
     */
    public void onTaskEvent(TodoEvent event) {
        
        // We check the type of event
        if (event.getType() == TodoEventType.TASK_ADDED) {
            taskList.add(new TodoTaskWrapper(event.getTask()));
        } else if (event.getType() == TodoEventType.TASK_DELETED) {

            // we have to find the wrapper that contain the
            // task that has been deleted in order to remove it
            taskList.remove(taskList.stream()
                    .filter(wrapper -> wrapper.getTask() == event.getTask())
                    .findFirst()
                    .orElse(null));
        }
    }

    @FXML
    public void onKeyTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTask();
        }
    }

    @FXML
    public void setAllTasksDone() {
        taskList.forEach(wrapper -> wrapper.doneProperty().setValue(true));

    }

    @FXML
    public void removeSelected() {
        todoService.removeTask(listView.getSelectionModel().getSelectedItem().getTask());
    }

    /*
        This is a TodoTaskCell.
        After hours struggling with ListCell and some Google researches,
        it cames out that this way of creating ListCell is the more
        stable and avoid most of common mistakes.
        
        In this case, we don't override the updateItem method.
        Instead, we just change the graphics whenever an item
        is mofidied.
    
        Let's come back now to our cell,
        It's a Hbox with a Label and checkbox inside.
        We want the checkbox to be updated when changing a
        TodoWrapper and we want our TodoWrapper to change
        when checking a Checkbox so we need to do a birectional binding.
    
       
     */
    public class TodoTaskCell extends ListCell<TodoTaskWrapper> {

        // label displaying the text of the task
        Label label = new Label();

        // checkbox displaying the status of the task (done/ not done)
        CheckBox checkbox = new CheckBox();

        // hbox containing everything
        HBox box = new HBox(checkbox, label);

        public TodoTaskCell() {
            box.getStyleClass().add("list-cell");
            
            // 
            itemProperty().addListener(this::onItemChanged);
        }

        public void onItemChanged(Observable obs, TodoTaskWrapper oldValue, TodoTaskWrapper newValue) {
            
            // 
            
            // a same cell can be used for different Wrapper, must make sure to desactivate previous bidirectional binding
            if (oldValue != null) {
                checkbox.selectedProperty().unbindBidirectional(oldValue.doneProperty());
            }

            if (newValue == null) {
                setGraphic(null);
            } else {

                setGraphic(box);

                // we don't really listen to the text
                label.textProperty().setValue(newValue.getText());

                // it allows the checkbox to react to any change of wrapper and vis versa
                checkbox.selectedProperty().bindBidirectional(newValue.doneProperty());

            }
        }
    }
}
