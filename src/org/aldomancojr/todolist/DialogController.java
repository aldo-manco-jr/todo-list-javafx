package org.aldomancojr.todolist;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.aldomancojr.todolist.data_model.ToDoData;
import org.aldomancojr.todolist.data_model.ToDoItem;

import java.time.LocalDate;

/**
 * Created by unieuro on 7/23/2019.
 */
public class DialogController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateField;

    public ToDoItem processResult(){
        String title = titleField.getText().trim(),
                description = descriptionField.getText().trim();
        LocalDate date = dateField.getValue();

        ToDoItem newItem = new ToDoItem(title, description, date);
        ToDoData.getSingleton().addToDoItem(newItem);
        return newItem;
    }
}
