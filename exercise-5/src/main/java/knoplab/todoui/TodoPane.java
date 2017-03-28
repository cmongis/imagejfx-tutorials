package knoplab.todoui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import knoplab.todo.TaskPriority;
import knoplab.todo.TodoAddedEvent;
import knoplab.todo.TodoDeletedEvent;
import knoplab.todo.TodoModifiedEvent;
import knoplab.todo.TodoSelectionChangedEvent;
import knoplab.todo.TodoService;
import knoplab.todo.TodoTask;
import knoplab.todo.TodoView;
import knoplab.todo.actions.TodoAction;
import org.scijava.event.EventHandler;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 *
 * The TodoUi uses now a SciJavaServices in order to deal with the data. What
 * are the advantages of using a TodoService hidden behind an interface ? - The
 * view doesn't care what happens behind and only react to change of the model -
 * we could easily create a TodoService that fetch a Todo List from the internet
 * and that would change nothing for the view.
 *
 * @author Cyril MONGIS
 */
public class TodoPane extends BorderPane implements TodoView {

    @FXML
    TextField textField; // note that it's okay to have such a simple name if your controller only contains one text field

    @FXML
    ListView<TodoTask> listView;

    /**
     * the toolbar holding our buttons
     */
    @FXML
    ToolBar buttonBar;

    /**
     * contains all the cell list ever created (may lead to memory leaks, not
     * recommended)
     */
    List<TodoTaskCell> cellList = new ArrayList<>();

    /**
     * Holds the priority possibilities
     */
    @FXML
    ComboBox<TaskPriority> priorityComboBox;

    @Parameter
    TodoService todoService;

    public TodoPane() throws IOException {

        // To sum up, we extends a BorderPane and we inject an FXML
        // inside it.
        // In this way, the Java class become the center point, not the FXML.
        // Be careful that the option use fx:root is ticked in the SceneBuilder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TodoUI.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        // setting the cell factory
        listView.setCellFactory(this::createCell);

        // when ENTER is pressed, we want to add the todo task so we listen to every 
        // key released event.
        textField.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyTyped);

        // populating the priority box
        priorityComboBox
                .getItems()
                .addAll(TaskPriority.IMPORTANT, TaskPriority.NORMAL, TaskPriority.TOMORROW);
        priorityComboBox.setValue(TaskPriority.NORMAL);

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // adding the css
        refreshCss(null);

    }

    // our factory is just a method that creates a TodoTaskCell
    private ListCell<TodoTask> createCell(ListView<TodoTask> listView) {
        TodoTaskCell todoTaskCell = new TodoTaskCell();
        cellList.add(todoTaskCell);
        return todoTaskCell;
    }

    private void addTask() {

        // the todo service will later notify the UI that a task has been added
        todoService.addTask(textField.getText(), priorityComboBox.getValue());
        textField.setText("");

    }

    private void refreshList() {
        listView.getItems().clear();
        listView.getItems().addAll(todoService.getTaskList());
    }

    /*
        Overriden methods that controls the selection of the view
    */
    
    
    @Override
    public void select(List<TodoTask> taskList) {

        int[] indexes = taskList
                .stream()
                .mapToInt(task -> listView.getItems().indexOf(task))
                .toArray();

        if (indexes.length > 0) {
            listView.getSelectionModel().selectIndices(indexes[0], indexes);
        }
    }

    @Override
    public void setSelection(TodoTask task, boolean selection) {
        if (selection) {
            listView.getSelectionModel().select(task);
        } else {
            listView.getSelectionModel().clearSelection(listView.getItems().indexOf(task));
        }
    }


    /*
        FXML Actions
    
     */
    @FXML
    public void onKeyTyped(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTask();
        }
    }

    /*
        SciJava Events

     */
    @EventHandler
    public void onTaskAdded(TodoAddedEvent event) {
        Platform.runLater(this::refreshList);
    }

    @EventHandler
    public void onTaskDeletedEvent(TodoDeletedEvent event) {
        Platform.runLater(this::refreshList);
    }

    /**
     * If a task has been modified, we find the cell that contain it and refresh
     * it. Note that the event handler could also be added inside the cell
     * itself.
     *
     * @param event
     */
    @EventHandler
    public void onTaskModified(TodoModifiedEvent event) {
        cellList
                .stream()
                .filter(cell -> cell.getItem() == event.getTask())
                .forEach(TodoTaskCell::refresh);
    }

    @Override
    public void init() {

        buttonBar
                .getItems()
                .addAll(
                        todoService
                                .getActionList()
                                .stream()
                                .map(this::createButton)
                                .collect(Collectors.toList())
                );

        // Adding refresh css button
        buttonBar.getItems().add(
                new ButtonBuilder()
                        .setLabel("Refresh Css")
                        .setAction(this::refreshCss)
                        .build()
        );

        Platform
                .runLater(this::refresh);
    }

    private Button createButton(TodoAction action) {
        Button button = new Button();

        button.setText(action.getClass().getAnnotation(Plugin.class).label());
        button.setOnAction(event -> {
            todoService.execute(action);
        });
        return button;
    }

    private void refreshCss(ActionEvent event) {
        
        getStylesheets().clear();
        getStylesheets().add(getClass().getResource("TodoUI.css").toExternalForm());

    }

    @Override
    public List<TodoTask> getSelectedTask() {
        return listView.getSelectionModel().getSelectedItems();
    }

    @Override
    public void show() {

    }

    public void refresh() {
        listView.getItems().clear();
        listView.getItems().addAll(todoService.getTaskList());
    }

    @Override
    public boolean isSelected(TodoTask task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class TodoTaskCell extends ListCell<TodoTask> {

        // label displaying the text of the task
        Label label = new Label();

        // checkbox displaying the status of the task (done/ not done)
        CheckBox checkbox = new CheckBox();

        // hbox containing everything
        HBox box = new HBox(checkbox, label);

        private JavaBeanProperty<Boolean> doneProperty;

        public TodoTaskCell() {

            getStyleClass().add("task-cell");

            itemProperty().addListener(this::onItemChanged);
            checkbox.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBoxClicked);
            //this.addEventFilter(MouseEvent.MOUSE_CLICKED, this::onItemClicked);
        }

        public void onItemChanged(Observable obs, TodoTask oldValue, TodoTask newValue) {

            if (newValue == null) {
                setGraphic(null);
            } else {

                setGraphic(box);

                // we don't really listen to the text
                label.textProperty().setValue(newValue.getText());

                refresh();

            }
        }

        private void updateCss() {

            checkPriorityClass(TaskPriority.NORMAL);
            checkPriorityClass(TaskPriority.IMPORTANT);
            checkPriorityClass(TaskPriority.TOMORROW);

        }

        /**
         * If the current item has the given priority, the list cell will be
         * given the corresponding CSS class
         *
         * @param priority Priority of the task
         */
        private void checkPriorityClass(TaskPriority priority) {
            toggleClass(priority.toString().toLowerCase(), getItem() != null && getItem().getPriority().equals(priority));
        }

        /**
         * Toggles a CSS class
         *
         * @param cssClass
         * @param toggle
         */
        private void toggleClass(String cssClass, boolean toggle) {

            if (toggle && getStylesheets().contains(cssClass) == false) {
                getStyleClass().add(cssClass);
            } else {
                getStyleClass().remove(cssClass);
            }

        }

        public void onBoxClicked(MouseEvent event) {
            if (event.getTarget() != checkbox) {
                return;
            }
            getItem().setDone(!getItem().isDone());
            event.consume();
            refresh();

        }

        public void refresh() {
            if (getItem() == null) {
                return;
            }
            updateCss();
            checkbox.setSelected(getItem().isDone());

        }

    }

    private class ButtonBuilder {

        final Button button = new Button();

        public ButtonBuilder setLabel(String label) {
            button.setText(label);
            return this;
        }

        public ButtonBuilder setAction(javafx.event.EventHandler<ActionEvent> handler) {
            button.setOnAction(handler);
            return this;
        }

        public ButtonBuilder setAction(Runnable runnable) {
            button.setOnAction(event -> runnable.run());
            return this;
        }

        public Button build() {
            return button;
        }

    }
}
