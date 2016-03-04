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

    TodoService todoService = new DefaultTodoService();

    ObservableList<TodoTaskWrapper> taskList = FXCollections.observableArrayList();

    @FXML
    TextField textField; // note that it's okay to have such a simple name if your controller only contains one text field

    @FXML
    ListView<TodoTaskWrapper> listView;

    @FXML
    ListView<TodoTaskWrapper> todoTaskWrapper;

    public TodoUi() throws IOException {

        // this method allows you to use Controller as main class and UI element
        // Be careful that the option use fx:root is ticked in the SceneBuilder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/knoplab/todoui/TodoUi.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

        // initializing the service;
        todoService.addTaskListener(this::onTaskEvent);

        listView.setCellFactory(this::createCell);

        listView.setItems(taskList);
        textField.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyTyped);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> System.out.println(newValue));
    }

    private ListCell<TodoTaskWrapper> createCell(ListView<TodoTaskWrapper> listView) {
        System.out.println("creating a new cell");
        return new TodoTaskCell();
    }

    private void addTask() {

        // the todo service will later notify the UI that a task has been added
        todoService.addTask(textField.getText());
        textField.setText("");

    }

    /*
        Events
     */
    public void onTaskEvent(TodoEvent event) {

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
        todo task cell
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

            itemProperty().addListener(this::onItemChanged);

        }

        public void onItemChanged(Observable obs, TodoTaskWrapper oldValue, TodoTaskWrapper newValue) {

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
