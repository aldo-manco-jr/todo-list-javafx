package org.aldomancojr.todolist;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.aldomancojr.todolist.data_model.ToDoData;
import org.aldomancojr.todolist.data_model.ToDoItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    //private List<ToDoItem> toDoList_Data;
    @FXML
    private ListView<ToDoItem> toDoList_UI;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label deadlineLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton isFiltering;

    private FilteredList<ToDoItem> filteredList;
    private Predicate<ToDoItem> allItems;
    private Predicate<ToDoItem> todayItems;

    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem selectedItem = toDoList_UI.getSelectionModel().getSelectedItem();
                deleteItem(selectedItem);
            }
        });
        listContextMenu.getItems().add(deleteMenuItem);

    /*    ToDoItem item1 = new ToDoItem("short1", "details1", LocalDate.of(2019, Month.FEBRUARY, 15));
        ToDoItem item2 = new ToDoItem("short2", "details2", LocalDate.of(2018, Month.DECEMBER, 4));
        ToDoItem item3 = new ToDoItem("short3", "details3", LocalDate.of(2019, Month.JULY, 10));
        ToDoItem item4 = new ToDoItem("short4", "details4", LocalDate.of(2019, Month.JANUARY, 29));

        toDoList_Data = new ArrayList<>(asList(item1, item2, item3, item4));
        Collections.sort(toDoList_Data);

        ToDoData.getSingleton().setListToDoItems(toDoList_Data);*/

        toDoList_UI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if (newValue != null) {
                    ToDoItem tmp = toDoList_UI.getSelectionModel().getSelectedItem();
                    descriptionTextArea.setText(tmp.getDetails());
                    DateTimeFormatter datetime = DateTimeFormatter.ofPattern("eeee, MMMM d, yyyy");
                    deadlineLabel.setText(datetime.format(tmp.getDeadline()));
                }
            }
        });

        allItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return true;
            }
        };

        todayItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return toDoItem.getDeadline().isEqual(LocalDate.now());
            }
        };

        filteredList = new FilteredList<ToDoItem>(ToDoData.getSingleton().getListToDoItems(), allItems);

        SortedList<ToDoItem> sortedList = new SortedList<ToDoItem>(filteredList, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        //toDoList_UI.getItems().addAll(ToDoData.getSingleton().getListToDoItems());
        toDoList_UI.setItems(sortedList);
        toDoList_UI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoList_UI.getSelectionModel().selectFirst();

        toDoList_UI.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell<ToDoItem> listCell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: bold");
                            } else if (item.getDeadline().isAfter(LocalDate.now())) {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };

                listCell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty){
                                listCell.setContextMenu(null);
                            }else {
                                listCell.setContextMenu(listContextMenu);
                            }
                        });
                return listCell;
            }
        });
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Add a New ToDo Item");
        dialog.setHeaderText("Get in Title, Description & Deadline of ToDo Item");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("todoitem_dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            DialogController dialogController = loader.getController();
            ToDoItem newItem = dialogController.processResult();
            //toDoList_UI.getItems().setAll(ToDoData.getSingleton().getListToDoItems());
            System.out.println("OK");
            toDoList_UI.getSelectionModel().select(newItem);
        } else {
            System.out.println("Cancel");
        }
    }

    /*public void handleClickOnListViewItem() {
        ToDoItem current = (ToDoItem) toDoList_UI.getSelectionModel().getSelectedItem();
        deadlineLabel.setText(current.getDeadline().toString());
        descriptionTextArea.setText(current.getDetails());
    }*/

    @FXML
    public void handleKeyPressed(KeyEvent keyPressed){
        ToDoItem item = toDoList_UI.getSelectionModel().getSelectedItem();
        if (item!=null){
            if (keyPressed.getCode() == KeyCode.DELETE){
                deleteItem(item);
            }
        }
    }

    public void eventHandlerDeleteItem(){
        ToDoItem item = toDoList_UI.getSelectionModel().getSelectedItem();
        if (item!=null){
            deleteItem(item);
        }
    }

    public void deleteItem(ToDoItem itemToDelete){
        Alert confermation = new Alert(Alert.AlertType.CONFIRMATION);
        confermation.setHeaderText("ToDo Item Delete Panel");
        confermation.setContentText("Are you sure? Press \"OK\" to delete, or \"Cancel\"");

        Optional<ButtonType> choice = confermation.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK){
            ToDoData.getSingleton().deleteToDoItem(itemToDelete);
        }
    }

    public void handleFilterButton(){
        ToDoItem item = toDoList_UI.getSelectionModel().getSelectedItem();
        if (isFiltering.isSelected()){
            filteredList.setPredicate(todayItems);
            if (filteredList.isEmpty()){
                descriptionTextArea.clear();
                deadlineLabel.setText("");
            }else if (filteredList.contains(item)){
                toDoList_UI.getSelectionModel().select(item);
            }else {
                toDoList_UI.getSelectionModel().selectFirst();
            }
        }else {
            filteredList.setPredicate(allItems);
            toDoList_UI.getSelectionModel().select(item);
        }
    }

    public void handleExit(){
        Platform.exit();
    }

    public void exitThroughKey(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ESCAPE){
            Platform.exit();
        }
    }
}